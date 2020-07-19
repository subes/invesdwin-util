package de.invesdwin.util.math.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.error.UnknownArgumentException;
import de.invesdwin.util.math.Booleans;
import de.invesdwin.util.math.decimal.ADecimal;

@Immutable
public final class CheckedCastBitSets {

    private CheckedCastBitSets() {
    }

    //CHECKSTYLE:OFF
    public static BitSet checkedCastVector(final Object value) {
        //CHECKSTYLE:ON
        if (value == null) {
            return null;
        } else if (value instanceof byte[]) {
            final byte[] cValue = (byte[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Byte[]) {
            final Byte[] cValue = (Byte[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof boolean[]) {
            final boolean[] cValue = (boolean[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Boolean[]) {
            final Boolean[] cValue = (Boolean[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof double[]) {
            final double[] cValue = (double[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Double[]) {
            final Double[] cValue = (Double[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof float[]) {
            final float[] cValue = (float[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Float[]) {
            final Float[] cValue = (Float[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof long[]) {
            final long[] cValue = (long[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Long[]) {
            final Long[] cValue = (Long[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof int[]) {
            final int[] cValue = (int[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Integer[]) {
            final Integer[] cValue = (Integer[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof short[]) {
            final short[] cValue = (short[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Short[]) {
            final Short[] cValue = (Short[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof ADecimal[]) {
            final ADecimal<?>[] cValue = (ADecimal<?>[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof BigDecimal[]) {
            final BigDecimal[] cValue = (BigDecimal[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof BigInteger[]) {
            final BigInteger[] cValue = (BigInteger[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Iterable) {
            final Iterable<?> cValue = (Iterable<?>) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Iterator) {
            final Iterator<?> cValue = (Iterator<?>) value;
            return checkedCastVector(cValue);
        } else if (value instanceof char[]) {
            final char[] cValue = (char[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Character[]) {
            final Character[] cValue = (Character[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof CharSequence) {
            final CharSequence cValue = (CharSequence) value;
            return checkedCastVector(cValue);
        } else if (value instanceof CharSequence[]) {
            final CharSequence[] cValue = (CharSequence[]) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Object[]) {
            final Object[] cValue = (Object[]) value;
            return checkedCastVector(cValue);
        } else {
            throw UnknownArgumentException.newInstance(Object.class, value);
        }
    }

    public static BitSet checkedCastVector(final Object[] value) {
        if (value == null) {
            return null;
        }
        if (value.length == 1) {
            final Object firstValue = value[0];
            if (firstValue != null && firstValue.getClass().isArray()) {
                return checkedCastVector(firstValue);
            }
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Boolean[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final boolean[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Iterator<?> value) {
        if (value == null) {
            return null;
        }
        final List<Object> list = new ArrayList<Object>();
        while (value.hasNext()) {
            list.add(value.next());
        }
        return checkedCastVector(list);
    }

    public static BitSet checkedCastVector(final Iterable<?> value) {
        if (value == null) {
            return null;
        } else if (value instanceof List) {
            final List<?> cValue = (List<?>) value;
            return checkedCastVector(cValue);
        } else if (value instanceof Collection) {
            final Collection<?> cValue = (Collection<?>) value;
            return checkedCastVector(cValue);
        }
        return checkedCastVector(value.iterator());
    }

    public static BitSet checkedCastVector(final List<?> value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.size());
        for (int i = 0; i < value.size(); i++) {
            if (Booleans.checkedCast(value.get(i))) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Collection<?> value) {
        if (value == null) {
            return null;
        } else if (value instanceof List) {
            final List<?> cValue = (List<?>) value;
            return checkedCastVector(cValue);
        }
        final BitSet vector = new BitSet(value.size());
        final Iterator<?> iterator = value.iterator();
        for (int i = 0; i < value.size(); i++) {
            if (Booleans.checkedCast(iterator.next())) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final byte[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Byte[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Character[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final char[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Short[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final short[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final int[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Integer[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Long[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final long[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Float[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final float[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final Double[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final double[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final ADecimal<?>[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final BigDecimal[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    public static BitSet checkedCastVector(final BigInteger[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    private static BitSet checkedCastVector(final CharSequence value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length());
        for (int i = 0; i < value.length(); i++) {
            if (Booleans.checkedCast(value.charAt(i))) {
                vector.set(i);
            }
        }
        return vector;
    }

    private static BitSet checkedCastVector(final CharSequence[] value) {
        if (value == null) {
            return null;
        }
        final BitSet vector = new BitSet(value.length);
        for (int i = 0; i < value.length; i++) {
            if (Booleans.checkedCast(value[i])) {
                vector.set(i);
            }
        }
        return vector;
    }

    //CHECKSTYLE:OFF
    public static BitSet[] checkedCastMatrix(final Object value) {
        //CHECKSTYLE:ON
        if (value == null) {
            return null;
        } else if (value instanceof byte[][]) {
            final byte[][] cValue = (byte[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Byte[][]) {
            final Byte[][] cValue = (Byte[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof boolean[][]) {
            final boolean[][] cValue = (boolean[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Boolean[][]) {
            final Boolean[][] cValue = (Boolean[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof double[][]) {
            final double[][] cValue = (double[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Double[][]) {
            final Double[][] cValue = (Double[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof float[][]) {
            final float[][] cValue = (float[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Float[][]) {
            final Float[][] cValue = (Float[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof long[][]) {
            final long[][] cValue = (long[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Long[][]) {
            final Long[][] cValue = (Long[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof int[][]) {
            final int[][] cValue = (int[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Integer[][]) {
            final Integer[][] cValue = (Integer[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof short[][]) {
            final short[][] cValue = (short[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Short[][]) {
            final Short[][] cValue = (Short[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof char[][]) {
            final char[][] cValue = (char[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Character[][]) {
            final Character[][] cValue = (Character[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof ADecimal[][]) {
            final ADecimal<?>[][] cValue = (ADecimal<?>[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof BigDecimal[][]) {
            final BigDecimal[][] cValue = (BigDecimal[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof BigInteger[][]) {
            final BigInteger[][] cValue = (BigInteger[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof CharSequence[]) {
            final CharSequence[] cValue = (CharSequence[]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof CharSequence[][]) {
            final CharSequence[][] cValue = (CharSequence[][]) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Iterable) {
            final Iterable<?> cValue = (Iterable<?>) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Iterator) {
            final Iterator<?> cValue = (Iterator<?>) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Object[]) {
            final Object[] cValue = (Object[]) value;
            return checkedCastMatrix(cValue);
        } else {
            throw UnknownArgumentException.newInstance(Object.class, value);
        }
    }

    public static BitSet[] checkedCastMatrix(final Object[] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Iterator<?> value) {
        if (value == null) {
            return null;
        }
        final List<Object> list = new ArrayList<Object>();
        while (value.hasNext()) {
            list.add(value.next());
        }
        return checkedCastMatrix(list);
    }

    public static BitSet[] checkedCastMatrix(final Iterable<?> value) {
        if (value == null) {
            return null;
        } else if (value instanceof List) {
            final List<?> cValue = (List<?>) value;
            return checkedCastMatrix(cValue);
        } else if (value instanceof Collection) {
            final Collection<?> cValue = (Collection<?>) value;
            return checkedCastMatrix(cValue);
        }
        return checkedCastMatrix(value.iterator());
    }

    public static BitSet[] checkedCastMatrix(final List<?> value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.size()];
        for (int row = 0; row < value.size(); row++) {
            matrix[row] = checkedCastVector(value.get(row));
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Collection<?> value) {
        if (value == null) {
            return null;
        } else if (value instanceof List) {
            final List<?> cValue = (List<?>) value;
            return checkedCastMatrix(cValue);
        }
        final BitSet[] matrix = new BitSet[value.size()];
        final Iterator<?> iterator = value.iterator();
        for (int row = 0; row < value.size(); row++) {
            matrix[row] = checkedCastVector(iterator.next());
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Byte[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final byte[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Boolean[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final boolean[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Character[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final char[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Short[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final short[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Integer[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final int[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Long[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final long[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Float[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final float[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final Double[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final double[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final ADecimal<?>[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final BigDecimal[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    public static BitSet[] checkedCastMatrix(final BigInteger[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    private static BitSet[] checkedCastMatrix(final CharSequence[] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

    private static BitSet[] checkedCastMatrix(final CharSequence[][] value) {
        if (value == null) {
            return null;
        }
        final BitSet[] matrix = new BitSet[value.length];
        for (int row = 0; row < value.length; row++) {
            matrix[row] = checkedCastVector(value[row]);
        }
        return matrix;
    }

}
