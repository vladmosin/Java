package com.hse.cw1;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private int size;
    private Object list;

    public SmartList() {
        size = 0;
        list = null;
    }

    public SmartList(Collection<? extends E> collection) {
        size = collection.size();
        if (size == 1) {
            for (var element : collection) {
                list = element;
            }
        } else if (size < 6) {
            list = (E[]) new Object[5];
            int index = 0;

            for (var element : collection) {
                ((E[])list)[index] = element;
                index++;
            }
        } else {
            list = new ArrayList<E>();

            for (var element : collection) {
                ((ArrayList) list).add(element);
            }
        }
    }

    @Override
    public E get(int index) {
        if (size <= index) {
            throw new IllegalArgumentException("illegal index");
        } else if (size == 1) {
            return (E)list;
        } else if (size <= 5) {
            return ((E[])list)[index];
        } else {
            return (E)((ArrayList)list).get(index);
        }
    }

    @Override
    public E set(int index, E element) {
        if (size <= index) {
            throw new IllegalArgumentException("illegal index");
        } else if (size == 1) {
            E resultElement = (E)list;
            list = element;
            return resultElement;
        } else if (size <= 5) {
            E resultElement = ((E[])list)[index];
            ((E[])list)[index] = element;
            return resultElement;
        } else {
            return (E) ((ArrayList)list).set(index, element);
        }
    }

    public int findIndex(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("element is null");
        }
        if (size == 0) {
            return -1;
        } else if (size == 1) {
            return element.equals(list) ? 0 : -1;
        } else if (size <= 5) {
            return findInArray(element);
        } else {
            return ((ArrayList)list).indexOf(element);
        }
    }

    private int findInArray(Object element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(((E[])list)[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean add(E element) {
        size++;
        if (size == 0) {
            list = element;
        } else if (size == 1) {
            E firstElement = (E) list;
            list = (E[])new Object[5];
            ((E[])list)[0] = firstElement;
            ((E[])list)[1] = element;

        } else if (size < 5) {
            ((E[])list)[size] = element;
        } else if (size == 5) {
            var arrayList = new ArrayList<E>();

            for (int i = 0; i < size; i++) {
                arrayList.add(((E[])list)[i]);
            }

            arrayList.add(element);
            list = arrayList;
        } else {
            ((ArrayList)list).add(element);
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = findIndex(element);

        size--;
        if (index == -1) {
            size++;
            return false;
        } else if (size == 1) {
            list = null;
        } else if (size == 2) {
            list = ((E[])list)[1 - index];
        } else if (size <= 5) {
            removeFromArray(index);
        }

        return true;
    }

    private void removeFromArray(int index) {
        for (int i = index + 1; i < 5; i++) {
            ((E[])list)[i] = ((E[])list)[i + 1];
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    public class SmartListIterator implements Iterator {
        private int index;

        public SmartListIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new IllegalArgumentException("no next");
            }

            index++;
            return get(index - 1);
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new SmartListIterator();
    }

    @Override
    public void clear() {
        size = 0;
        list = null;
    }
}
