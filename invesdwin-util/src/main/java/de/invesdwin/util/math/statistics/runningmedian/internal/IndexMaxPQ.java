package de.invesdwin.util.math.statistics.runningmedian.internal;

import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.bean.tuple.ImmutableEntry;

/**
 * https://algs4.cs.princeton.edu/24pq/IndexMaxPQ.java.html
 * 
 * The {@code IndexMaxPQ} class represents an indexed priority queue of generic keys. It supports the usual
 * <em>insert</em> and <em>delete-the-maximum</em> operations, along with <em>delete</em> and <em>change-the-key</em>
 * methods. In order to let the client refer to items on the priority queue, an integer between {@code 0} and
 * {@code maxN - 1} is associated with each key—the client uses this integer to specify which key to delete or change.
 * It also supports methods for peeking at a maximum key, testing if the priority queue is empty, and iterating through
 * the keys.
 * <p>
 * This implementation uses a binary heap along with an array to associate keys with integers in the given range. The
 * <em>insert</em>, <em>delete-the-maximum</em>, <em>delete</em>, <em>change-key</em>, <em>decrease-key</em>, and
 * <em>increase-key</em> operations take logarithmic time. The <em>is-empty</em>, <em>size</em>, <em>max-index</em>,
 * <em>max-key</em>, and <em>key-of</em> operations take constant time. Construction takes time proportional to the
 * specified capacity.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of <i>Algorithms, 4th
 * Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Key>
 *            the generic type of key on this priority queue
 * 
 */
@NotThreadSafe
public class IndexMaxPQ<Key extends Comparable<Key>> implements Iterable<Entry<Integer, Key>>, Cloneable {
    private int n; // number of elements on PQ
    private int[] pq; // binary heap using 1-based indexing
    private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys; // keys[i] = priority of i

    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} and {@code maxN - 1}.
     *
     * @param maxN
     *            the keys on this priority queue are index from {@code 0} to {@code maxN - 1}
     * @throws IllegalArgumentException
     *             if {@code maxN < 0}
     */
    @SuppressWarnings("unchecked")
    public IndexMaxPQ(final int maxN) {
        if (maxN < 0) {
            throw new IllegalArgumentException();
        }
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1]; // make this of length maxN??
        pq = new int[maxN + 1];
        qp = new int[maxN + 1]; // make this of length maxN??
        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1;
        }
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param i
     *            an index
     * @return {@code true} if {@code i} is an index on this priority queue; {@code false} otherwise
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     */
    public boolean contains(final int i) {
        return qp[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return n;
    }

    /**
     * Associate key with index i.
     *
     * @param i
     *            an index
     * @param key
     *            the key to associate with index {@code i}
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException
     *             if there already is an item associated with index {@code i}
     */
    public void insert(final int i, final Key key) {
        if (contains(i)) {
            throw new IllegalArgumentException("index is already in the priority queue");
        }
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    /**
     * Returns an index associated with a maximum key.
     *
     * @return an index associated with a maximum key
     * @throws NoSuchElementException
     *             if this priority queue is empty
     */
    public int maxIndex() {
        if (n == 0) {
            throw new java.util.NoSuchElementException("Priority queue underflow");
        }
        return pq[1];
    }

    /**
     * Returns a maximum key.
     *
     * @return a maximum key
     * @throws NoSuchElementException
     *             if this priority queue is empty
     */
    public Key maxKey() {
        if (n == 0) {
            throw new java.util.NoSuchElementException("Priority queue underflow");
        }
        return keys[pq[1]];
    }

    /**
     * Removes a maximum key and returns its associated index.
     *
     * @return an index associated with a maximum key
     * @throws NoSuchElementException
     *             if this priority queue is empty
     */
    public Entry<Integer, Key> delMax() {
        if (n == 0) {
            throw new java.util.NoSuchElementException("Priority queue underflow");
        }
        final int min = pq[1];
        exch(1, n--);
        sink(1);
        final Key minKey = keys[min];

        assert pq[n + 1] == min;
        qp[min] = -1; // delete
        keys[min] = null; // to help with garbage collection
        pq[n + 1] = -1; // not needed
        return ImmutableEntry.of(min, minKey);
    }

    /**
     * Returns the key associated with index {@code i}.
     *
     * @param i
     *            the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException
     *             no key is associated with index {@code i}
     */
    public Key keyOf(final int i) {
        if (!contains(i)) {
            throw new java.util.NoSuchElementException("index is not in the priority queue");
        } else {
            return keys[i];
        }
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
     * @param i
     *            the index of the key to change
     * @param key
     *            change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     */
    public void changeKey(final int i, final Key key) {
        if (!contains(i)) {
            throw new java.util.NoSuchElementException("index is not in the priority queue");
        }
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * Increase the key associated with index {@code i} to the specified value.
     *
     * @param i
     *            the index of the key to increase
     * @param key
     *            increase the key associated with index {@code i} to this key
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException
     *             if {@code key <= keyOf(i)}
     * @throws NoSuchElementException
     *             no key is associated with index {@code i}
     */
    public void increaseKey(final int i, final Key key) {
        if (!contains(i)) {
            throw new java.util.NoSuchElementException("index is not in the priority queue");
        }
        if (keys[i].compareTo(key) >= 0) {
            throw new IllegalArgumentException(
                    "Calling increaseKey() with given argument would not strictly increase the key");
        }

        keys[i] = key;
        swim(qp[i]);
    }

    /**
     * Decrease the key associated with index {@code i} to the specified value.
     *
     * @param i
     *            the index of the key to decrease
     * @param key
     *            decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException
     *             if {@code key >= keyOf(i)}
     * @throws NoSuchElementException
     *             no key is associated with index {@code i}
     */
    public void decreaseKey(final int i, final Key key) {
        if (!contains(i)) {
            throw new java.util.NoSuchElementException("index is not in the priority queue");
        }
        if (keys[i].compareTo(key) <= 0) {
            throw new IllegalArgumentException(
                    "Calling decreaseKey() with given argument would not strictly decrease the key");
        }

        keys[i] = key;
        sink(qp[i]);
    }

    /**
     * Remove the key on the priority queue associated with index {@code i}.
     *
     * @param i
     *            the index of the key to remove
     * @throws IllegalArgumentException
     *             unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException
     *             no key is associated with index {@code i}
     */
    public void delete(final int i) {
        if (!contains(i)) {
            throw new java.util.NoSuchElementException("index is not in the priority queue");
        }
        final int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/
    private boolean less(final int i, final int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
    }

    private void exch(final int i, final int j) {
        final int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/
    private void swim(final int paramK) {
        int k = paramK;
        while (k > 1 && less(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(final int paramK) {
        int k = paramK;
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(j, j + 1)) {
                j++;
            }
            if (!less(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    /***************************************************************************
     * Iterators.
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on the priority queue in descending order. The iterator doesn't
     * implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in descending order
     */
    @Override
    public IndexMaxPQHeapIterator<Key> iterator() {
        return new IndexMaxPQHeapIterator<Key>(clone());
    }

    @SuppressWarnings("unchecked")
    @Override
    public IndexMaxPQ<Key> clone() {
        try {
            final IndexMaxPQ<Key> clone = (IndexMaxPQ<Key>) super.clone();
            clone.n = n;
            clone.pq = pq.clone();
            clone.qp = qp.clone();
            clone.keys = keys.clone();
            return clone;
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}