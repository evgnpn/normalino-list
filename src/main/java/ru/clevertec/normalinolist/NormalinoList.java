package ru.clevertec.normalinolist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NormalinoList<T> implements List<T> {

    private Node firstNode;
    private int size;

    public NormalinoList() {
    }

    public NormalinoList(Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(T element) {

        if (firstNode == null) {
            firstNode = new Node(element);
        } else {
            var lastNode = moveToIndex(size - 1);
            lastNode.setNext(new Node(element));
        }

        return size < ++size;
    }

    @Override
    public void add(int index, T element) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        var newNode = new Node(element);

        if (index == 0) {
            newNode.setNext(firstNode);
            firstNode = newNode;
        } else {
            var beforeNode = moveToIndex(index - 1);
            newNode.setNext(beforeNode.next());
            beforeNode.setNext(newNode);
        }

        size++;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {

        if (collection == null || collection.isEmpty()) {
            return false;
        }

        var nodesToAdd = createNodes(collection);

        if (isEmpty()) {
            firstNode = nodesToAdd.getFirstNode();
        } else {
            var lastNode = moveToIndex(size - 1);
            lastNode.setNext(nodesToAdd.getFirstNode());
        }

        size += collection.size();

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {

        if (collection == null || collection.isEmpty()) {
            return false;
        }

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        var nodesToAdd = createNodes(collection);

        if (index == 0) {
            nodesToAdd.getLastNode().setNext(firstNode);
            firstNode = nodesToAdd.getFirstNode();
        } else {
            var beforeNode = moveToIndex(index - 1);
            nodesToAdd.getLastNode().setNext(beforeNode.next());
            beforeNode.setNext(nodesToAdd.getFirstNode());
        }

        size += collection.size();
        return true;
    }

    @Override
    public T remove(int index) {

        throwIfIndexOutOfBounds(index);

        T prevValue;

        if (index == 0) {
            prevValue = firstNode.value();
            firstNode = firstNode.hasNext() ? firstNode.next() : null;
        } else {
            var beforeNode = moveToIndex(index - 1);
            prevValue = beforeNode.next().value();
            beforeNode.setNext(beforeNode.hasNext() ? beforeNode.next().next() : null);
        }

        size--;
        return prevValue;
    }

    @Override
    public boolean remove(Object o) {

        if (firstNode == null) {
            return false;
        }

        var currentNode = firstNode;

        if (currentNode.value().equals(o)) {
            firstNode = firstNode.next();
            return size > --size;
        }

        while (currentNode.hasNext()) {

            if (currentNode.next().value().equals(o)) {
                currentNode.setNext(currentNode.next().hasNext() ? currentNode.next().next() : null);
                return size > --size;
            }

            currentNode = currentNode.next();
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

        return this.stream().allMatch(collection::contains);
    }

    @Override
    public T get(int index) {
        throwIfIndexOutOfBounds(index);
        return moveToIndex(index).value();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {

        var it = (NodeValueIterator) listIterator();

        while (it.hasNext()) {
            var element = it.next();

            if (!collection.contains(element)) {
                it.remove();
                size--;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        firstNode = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null;
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

        var it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            if (it.next().equals(o)) {
                lastIndex = i;
            }
        }

        return lastIndex;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {

        if (fromIndex < 0 || fromIndex + 1 > size()
                || toIndex < fromIndex || toIndex + 1 > size()) {
            throw new IndexOutOfBoundsException();
        }

        var nodesCount = toIndex - fromIndex + 1;
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
        return stream().toArray(Object[]::new);
    }

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
    public Iterator<T> iterator() {
        return new NodeValueIterator(firstNode);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListNodeValueIterator(firstNode, 0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListNodeValueIterator(firstNode, index);
    }

    private Node moveToIndex(int index) {
        var currentNode = firstNode;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next();
        }
        return currentNode;
    }

    private NodePair createNodes(Collection<? extends T> collection) {

        var it = collection.iterator();

        var firstNode = new Node(it.next());
        var nodePair = new NodePair(firstNode, firstNode);

        while (it.hasNext()) {
            var newNode = new Node(it.next());
            nodePair.getLastNode().setNext(newNode);
            nodePair.setLastNode(newNode);
        }

        return nodePair;
    }

    private void throwIfIndexOutOfBounds(int index) {
        if (index < 0 || index + 1 > size()) {
            throw new IndexOutOfBoundsException();
        }
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
            throw new UnsupportedOperationException();
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

    private class NodePair {

        private Node firstNode;
        private Node lastNode;

        public NodePair() {
        }

        public NodePair(Node firstNode, Node lastNode) {
            this.firstNode = firstNode;
            this.lastNode = lastNode;
        }

        public Node getFirstNode() {
            return firstNode;
        }

        public void setFirstNode(Node firstNode) {
            this.firstNode = firstNode;
        }

        public Node getLastNode() {
            return lastNode;
        }

        public void setLastNode(Node lastNode) {
            this.lastNode = lastNode;
        }
    }
}


