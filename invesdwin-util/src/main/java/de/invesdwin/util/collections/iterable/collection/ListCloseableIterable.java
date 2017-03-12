package de.invesdwin.util.collections.iterable.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.collections.ADelegateList;
import de.invesdwin.util.collections.iterable.ICloseableIterable;
import de.invesdwin.util.collections.iterable.ICloseableIterator;
import de.invesdwin.util.lang.Reflections;

@Immutable
public class ListCloseableIterable<E> implements ICloseableIterable<E> {

    private static final Field ARRAYLIST_ELEMENTDATA_FIELD;
    private static final Class<?> SUBLIST_CLASS;
    private static final Field SUBLIST_OFFSET_FIELD;
    private static final Field SUBLIST_PARENT_FIELD;

    static {
        ARRAYLIST_ELEMENTDATA_FIELD = Reflections.findField(ArrayList.class, "elementData");
        Reflections.makeAccessible(ARRAYLIST_ELEMENTDATA_FIELD);
        SUBLIST_CLASS = Reflections.classForName("java.util.ArrayList$SubList");
        SUBLIST_OFFSET_FIELD = Reflections.findField(SUBLIST_CLASS, "offset");
        Reflections.makeAccessible(SUBLIST_OFFSET_FIELD);
        SUBLIST_PARENT_FIELD = Reflections.findField(SUBLIST_CLASS, "parent");
        Reflections.makeAccessible(SUBLIST_PARENT_FIELD);
    }

    private final List<? extends E> list;

    public ListCloseableIterable(final List<? extends E> list) {
        if (list instanceof ADelegateList) {
            this.list = ADelegateList.maybeUnwrapToRoot(list);
        } else {
            this.list = list;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ICloseableIterator<E> iterator() {
        /*
         * try to save us the effort for checking the mod count and bounds all the time by trying to directly go to the
         * underlying array
         */
        if (list instanceof ArrayList) {
            final E[] array = (E[]) Reflections.getField(ARRAYLIST_ELEMENTDATA_FIELD, list);
            return new ArrayCloseableIterator<E>(array, 0, list.size());
        } else if (list.getClass().equals(SUBLIST_CLASS)) {
            final ArrayList<E> parent = (ArrayList<E>) Reflections.getField(SUBLIST_PARENT_FIELD, list);
            final int offset = (Integer) Reflections.getField(SUBLIST_OFFSET_FIELD, list);
            final E[] array = (E[]) Reflections.getField(ARRAYLIST_ELEMENTDATA_FIELD, parent);
            return new ArrayCloseableIterator<E>(array, offset, list.size());
        } else {
            return new ListCloseableIterator<E>(list);
        }
    }

}
