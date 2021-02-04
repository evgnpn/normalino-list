package ru.clevertec.custom.list;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntFunction;

public final class SinglyLinkedList<T> implements List<T> {

    private final static int TAIL_I = 0, HEAD_I = 1;
    private Node tail, head;
    private int size;

    public SinglyLinkedList() {
    }

    public SinglyLinkedList(Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(T value) {
        return isEmpty()
                ? addToEmpty(value)
                : addAfterLast(value);
    }

    @Override
    public void add(int index, T value) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        if (isEmpty())
            addToEmpty(value);
        else if (index == size)
            addAfterLast(value);
        else if (index == 0)
            addBeforeFirst(value);
        else
            addBetween(index, value);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty())
            return false;
        return isEmpty()
                ? addToEmpty(collection)
                : addAfterLast(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty())
            return false;
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        if (isEmpty())
            addToEmpty(collection);
        else if (index == size)
            addAfterLast(collection);
        else if (index == 0)
            addBeforeFirst(collection);
        else
            addBetween(index, collection);
        return true;
    }

    @Override
    public T remove(int index) {
        throwIfIndexOutOfBounds(index);
        var it = new SinglyLinkedListIterator(0);
        while (it.hasNext()) {
            var value = it.next();
            if (it.index == index) {
                it.remove();
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (o != null) {
            var it = iterator();
            while (it.hasNext()) {
                var value = it.next();
                if (value.equals(o)) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection == null || collection.isEmpty())
            return false;
        var it = iterator();
        var removedCount = 0;
        while (it.hasNext()) {
            var value = it.next();
            if (collection.contains(value)) {
                it.remove();
                removedCount++;
            }
        }
        return removedCount > 0;
    }

    @Override
    public T set(int index, T value) {
        throwIfIndexOutOfBounds(index);
        T returnValue = null;
        if (index + 1 == size()) {
            returnValue = head.value;
            head.value = value;
        } else {
            var it = listIterator();
            for (var i = 0; i <= index; i++)
                returnValue = it.next();
            it.set(value);
        }
        return returnValue;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection == null)
            throw new NullPointerException();
        if (isEmpty() || collection.isEmpty())
            return false;
        for (var value : collection)
            if (!contains(value))
                return false;
        return true;
    }

    @Override
    public T get(int index) {
        throwIfIndexOutOfBounds(index);
        if (index + 1 == size())
            return head.value;
        var it = iterator();
        T returnValue = null;
        for (var i = 0; i <= index; i++)
            returnValue = it.next();
        return returnValue;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        var it = iterator();
        var retainedCount = 0;
        while (it.hasNext()) {
            var value = it.next();
            if (!collection.contains(value)) {
                it.remove();
                retainedCount++;
            }
        }
        return retainedCount > 0;
    }

    @Override
    public void clear() {
        tail = head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int indexOf(Object o) {
        var it = iterator();
        for (int i = 0; it.hasNext(); i++)
            if (it.next().equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        var lastIndex = -1;
        if (isEmpty())
            return lastIndex;
        if (head.value.equals(o))
            return size - 1;
        var it = new SinglyLinkedListIterator(0);
        for (int i = 0; i < size - 1; i++)
            if (it.next().equals(o))
                lastIndex = it.index;
        return lastIndex;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {

        if (fromIndex < 0 || fromIndex + 1 > size()
                || toIndex < fromIndex || toIndex > size()) {
            throw new IndexOutOfBoundsException();
        }

        var fromNode = moveToIndex(fromIndex);

        var list = new SinglyLinkedList<T>();

        var currentNode = fromNode;
        var nodesCount = toIndex - fromIndex;

        for (int i = 0; i < nodesCount; i++) {
            list.add(currentNode.value);
            currentNode = currentNode.next;
        }

        return list;
    }

    @Override
    public Object[] toArray() {
        var it = iterator();
        var arr = new Object[size];
        for (var i = 0; it.hasNext(); i++)
            arr[i] = it.next();
        return arr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(T1[] array) {
        if (array == null)
            throw new NullPointerException();
        if (array.length != size())
            throw new ArrayStoreException();
        var it = new SinglyLinkedListIterator(0);
        while (it.hasNext()) {
            var value = it.next();
            array[it.index] = (T1) value;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return toArray(generator.apply(size()));
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new SinglyLinkedListIterator(index);
    }

    private Node moveToIndex(int index) {
        var currentNode = tail;
        for (int i = 0; i < index; i++)
            currentNode = currentNode.next;
        return currentNode;
    }

    @SuppressWarnings("unchecked")
    private Node[] createNodes(Collection<? extends T> collection) {

        var it = collection.iterator();

        var newNode = new Node(it.next());
        var tail = newNode;
        var head = newNode;

        while (it.hasNext()) {
            newNode = new Node(it.next());
            head.next = newNode;
            head = newNode;
        }

        var arr = (Node[]) Array.newInstance(Node.class, 2);
        arr[TAIL_I] = tail;
        arr[HEAD_I] = head;

        return arr;
    }

    private void throwIfIndexOutOfBounds(int index) {
        if (index < 0 || index + 1 > size())
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size());
    }

    private boolean addToEmpty(T value) {
        tail = head = new Node(value);
        size++;
        return true;
    }

    private boolean addToEmpty(Collection<? extends T> collection) {
        var it = collection.iterator();
        addToEmpty(it.next());
        while (it.hasNext())
            addAfterLast(it.next());
        return true;
    }

    private boolean addAfterLast(T value) {
        var newNode = new Node(value);
        head.next = newNode;
        head = newNode;
        size++;
        return true;
    }

    private boolean addAfterLast(Collection<? extends T> collection) {
        collection.forEach(this::addAfterLast);
        return true;
    }

    private void addBetween(int index, T value) {
        var newNode = new Node(value);
        var beforeNode = moveToIndex(index - 1);
        newNode.next = beforeNode.next;
        beforeNode.next = newNode;
        size++;
    }

    private void addBetween(int index, Collection<? extends T> collection) {
        var pair = createNodes(collection);
        var beforeNode = moveToIndex(index - 1);
        pair[HEAD_I].next = beforeNode.next;
        beforeNode.next = pair[TAIL_I];
        size += collection.size();
    }

    private void addBeforeFirst(T value) {
        var newNode = new Node(value);
        newNode.next = tail;
        tail = newNode;
        size++;
    }

    private void addBeforeFirst(Collection<? extends T> collection) {
        var pair = createNodes(collection);
        pair[HEAD_I].next = tail;
        tail = pair[TAIL_I];
        size += collection.size();
    }

    protected class Node implements Serializable {

        private T value;
        private Node next;

        public Node(T value) {
            this.value = value;
        }
    }

    private class SinglyLinkedListIterator implements ListIterator<T> {

        public int index;
        private Node previousNode;
        private Node currentNode;

        public SinglyLinkedListIterator(int index) {
            this.currentNode = moveToIndex(index);
            this.index = index - 1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex() < size();
        }

        @Override
        public T next() {
            index++;
            T returnValue;
            if (index != 0) {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            returnValue = currentNode.value;
            return returnValue;
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public void remove() {
            if (index == 0) {
                tail = currentNode.next;
                if (tail == null)
                    head = null;
            } else {
                previousNode.next = currentNode != null ? currentNode.next : null;
                if (previousNode.next == null)
                    head = previousNode;
                index--;
            }
            size--;
        }

        @Override
        public void set(T value) {
            currentNode.value = value;
        }

        @Override
        public void add(T value) {
            throw new UnsupportedOperationException();
        }
    }
}
