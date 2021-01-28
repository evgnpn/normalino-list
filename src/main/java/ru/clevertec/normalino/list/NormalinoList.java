package ru.clevertec.normalino.list;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntFunction;

public class NormalinoList<T> implements List<T> {

    private final static int TAIL_I = 0;
    private final static int HEAD_I = 1;

    private Node tail;
    public Node head;
    private int size;

    public NormalinoList() {
    }

    public NormalinoList(Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(T element) {
        return isEmpty()
                ? addToEmpty(element)
                : addAfterLast(element);
    }

    @Override
    public void add(int index, T element) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            addToEmpty(element);
        } else if (index == size) {
            addAfterLast(element);
        } else if (index == 0) {
            addBeforeFirst(element);
        } else {
            addBetween(index, element);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends T> collection) {

        if (collection == null || collection.isEmpty()) {
            return false;
        }

        return isEmpty()
                ? addToEmpty((T[]) collection.toArray())
                : addAfterLast((T[]) collection.toArray());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {

        if (collection == null || collection.isEmpty()) {
            return false;
        }

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            addToEmpty((T[]) collection.toArray());
        } else if (index == size) {
            addAfterLast((T[]) collection.toArray());
        } else if (index == 0) {
            addBeforeFirst((T[]) collection.toArray());
        } else {
            addBetween(index, (T[]) collection.toArray());
        }

        return true;
    }

    @Override
    public T remove(int index) {

        throwIfIndexOutOfBounds(index);

        T deletedValue;

        if (index == 0) {
            deletedValue = tail.value();
            tail = tail.hasNext() ? tail.next() : (head = null);
        } else {
            var beforeNode = moveToIndex(index - 1);
            deletedValue = beforeNode.next().value();
            beforeNode.setNext(beforeNode.hasNext() ? beforeNode.next().next() : null);

            if (!beforeNode.hasNext()) {
                head = beforeNode;
            }
        }

        size--;
        return deletedValue;
    }

    @Override
    public boolean remove(Object o) {

        if (isEmpty()) {
            return false;
        }

        var start = tail;

        if (start.value().equals(o)) {
            tail = tail.next();
            if (tail == null || !tail.hasNext()) {
                head = tail;
            }
            size--;
            return true;
        }

        while (start.hasNext()) {

            if (start.next().value().equals(o)) {
                start.setNext(start.next().hasNext() ? start.next().next() : null);

                if (!start.hasNext()) {
                    head = start;
                }

                size--;
                return true;
            }

            start = start.next();
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {

        if (collection == null || collection.isEmpty()) {
            return false;
        }

        var it = iterator();

        var index = 0;
        var removedCount = 0;

        while (it.hasNext()) {

            var element = it.next();

            if (collection.contains(element)) {
                remove(index--);
                removedCount++;
            }

            index++;
        }

        return removedCount > 0;
    }

    @Override
    public T set(int index, T element) {

        throwIfIndexOutOfBounds(index);

        var node = moveToIndex(index);
        var prevValue = node.value();
        node.setValue(element);
        return prevValue;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return stream().anyMatch(a -> a.equals(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {

        if (collection == null) {
            throw new NullPointerException();
        }

        if (isEmpty() || collection.isEmpty()) {
            return false;
        }

        for (var element : collection) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public T get(int index) {
        throwIfIndexOutOfBounds(index);
        return moveToIndex(index).value();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection<?> collection) {

        var it = (NodeValueIterator) listIterator();

        var retainedCount = 0;

        while (it.hasNext()) {
            var element = it.next();

            if (!collection.contains(element)) {
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

        for (int i = 0; it.hasNext(); i++) {
            if (it.next().equals(o)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {

        var lastIndex = -1;

        var it = (ListNodeValueIterator) this.listIterator();

        while (it.hasNext()) {
            var element = it.next();
            if (element.equals(o)) {
                lastIndex = it.previousIndex();
            }
        }

        return lastIndex;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {

        if (fromIndex < 0 || fromIndex + 1 > size()
                || toIndex < fromIndex || toIndex > size()) {
            throw new IndexOutOfBoundsException();
        }

        var nodesCount = toIndex - fromIndex;
        var fromNode = moveToIndex(fromIndex);

        var list = new NormalinoList<T>();

        var currentNode = fromNode;

        for (int i = 0; i < nodesCount; i++) {
            list.add(currentNode.value());
            currentNode = currentNode.next();
        }

        return list;
    }

    @Override
    public Object[] toArray() {

        var it = iterator();

        var arr = new Object[size];

        for (int i = 0; it.hasNext(); i++) {
            arr[i] = it.next();
        }

        return arr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(T1[] array) {

        if (array == null) {
            throw new NullPointerException();
        }

        if (array.length != size()) {
            throw new ArrayStoreException();
        }

        var i = 0;
        for (var element : this) {
            array[i++] = (T1) element;
        }

        return array;
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return toArray(generator.apply(size()));
    }

    @Override
    public Iterator<T> iterator() {
        return new NodeValueIterator(tail);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListNodeValueIterator(tail, 0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListNodeValueIterator(tail, index);
    }

    private Node moveToIndex(int index) {
        var currentNode = tail;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next();
        }
        return currentNode;
    }

    @SuppressWarnings("unchecked")
    private Node[] createNodes(T... elements) {

        var newNode = new Node(elements[0]);
        var tail = newNode;
        var head = newNode;

        for (int i = 1; i < elements.length; i++) {
            newNode = new Node(elements[i]);
            head.setNext(newNode);
            head = newNode;
        }

        var arr = (Node[]) Array.newInstance(Node.class, 2);
        arr[TAIL_I] = tail;
        arr[HEAD_I] = head;

        return arr;
    }

    private void throwIfIndexOutOfBounds(int index) {
        if (index < 0 || index + 1 > size()) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size());
        }
    }

    @SafeVarargs
    private boolean addToEmpty(T... elements) {

        var pair = createNodes(elements);

        tail = pair[TAIL_I];
        head = pair[HEAD_I];

        size += elements.length;
        return true;
    }

    @SafeVarargs
    private boolean addAfterLast(T... elements) {

        var pair = createNodes(elements);

        head.setNext(pair[TAIL_I]);
        head = pair[HEAD_I];

        size += elements.length;
        return true;
    }

    @SafeVarargs
    private void addBetween(int index, T... elements) {

        var pair = createNodes(elements);

        var beforeNode = moveToIndex(index - 1);

        pair[HEAD_I].setNext(beforeNode.next());
        beforeNode.setNext(pair[TAIL_I]);

        size += elements.length;
    }

    @SafeVarargs
    private void addBeforeFirst(T... elements) {

        var pair = createNodes(elements);

        pair[HEAD_I].setNext(tail);
        tail = pair[TAIL_I];

        size += elements.length;
    }

    private class Node {

        private T value;
        private Node next;

        public Node(T value) {
            this.value = value;
        }

        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }

        public T value() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node next() {
            return next;
        }
    }

    private class NodeValueIterator implements Iterator<T> {

        private Node node;

        public NodeValueIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            T value = node.value();
            node = node.next();
            return value;
        }

        public Node getNode() {
            return node;
        }
    }

    private class ListNodeValueIterator extends NodeValueIterator implements ListIterator<T> {

        private int index;

        public ListNodeValueIterator(Node node, int index) {
            super(node);
            this.index = index;
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

        public int currentIndex() {
            return index;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public void remove() {
            index--;
            NormalinoList.this.remove(index);
        }

        @Override
        public void set(T element) {
            NormalinoList.this.set(index, element);
        }

        @Override
        public void add(T element) {
            NormalinoList.this.add(index, element);
        }

        @Override
        public T next() {
            index++;
            return super.next();
        }
    }
}
