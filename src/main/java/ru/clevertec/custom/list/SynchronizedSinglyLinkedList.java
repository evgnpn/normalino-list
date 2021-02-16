package ru.clevertec.custom.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntFunction;

public final class SynchronizedSinglyLinkedList<T> implements List<T> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final SinglyLinkedList<T> singlyLinkedList = new SinglyLinkedList<>();

    public SynchronizedSinglyLinkedList() {
    }

    public SynchronizedSinglyLinkedList(Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(T value) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.add(value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, T value) {
        readWriteLock.writeLock().lock();
        try {
            singlyLinkedList.add(index, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.addAll(collection);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.addAll(index, collection);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public T remove(int index) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.remove(index);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.remove(o);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.removeAll(collection);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public T set(int index, T value) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.set(index, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.contains(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.containsAll(collection);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public T get(int index) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.get(index);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.retainAll(collection);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        readWriteLock.writeLock().lock();
        singlyLinkedList.clear();
        readWriteLock.writeLock().unlock();
    }

    @Override
    public int size() {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.indexOf(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.lastIndexOf(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.subList(fromIndex, toIndex);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.toArray();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] array) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.toArray(array);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        readWriteLock.readLock().lock();
        try {
            return singlyLinkedList.toArray(generator);
        } finally {
            readWriteLock.readLock().unlock();
        }
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
        readWriteLock.readLock().lock();
        try {
            return copyList(singlyLinkedList, index).listIterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private static <T> SinglyLinkedList<T> copyList(SinglyLinkedList<T> singlyLinkedList, int index) {
        var copyList = new SinglyLinkedList<T>();
        var it = singlyLinkedList.listIterator(index);
        while (it.hasNext())
            copyList.add(it.next());
        return copyList;
    }
}
