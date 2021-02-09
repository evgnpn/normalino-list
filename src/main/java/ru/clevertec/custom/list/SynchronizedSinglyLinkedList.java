// 1. Методы добавления, удаления, изменения могут выполняться только последовательно (по очереди)
// 2. Методы чтения могут выполняться параллельно (разными потоками одновременно)
// 3. Итераторы (iterator, foreach) клонируются, что бы один поток который читает данные,
// не прочитал данные которые изменил/добавил/удалил другой поток

package ru.clevertec.custom.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntFunction;

public final class SynchronizedSinglyLinkedList<T> implements List<T> {

    private final ReadWriteLock readWriteLock;
    private final SinglyLinkedList<T> singlyLinkedList = new SinglyLinkedList<>();

    public SynchronizedSinglyLinkedList() {
        readWriteLock = new ReentrantReadWriteLock();
    }

    public SynchronizedSinglyLinkedList(ReadWriteLock readWriteLock) {
        throwIfNull(readWriteLock, "readWriteLock");
        this.readWriteLock = readWriteLock;
    }

    public SynchronizedSinglyLinkedList(Collection<? extends T> collection) {
        readWriteLock = new ReentrantReadWriteLock();
        addAll(collection);
    }

    public SynchronizedSinglyLinkedList(Collection<? extends T> collection, ReadWriteLock readWriteLock) {
        throwIfNull(readWriteLock, "readWriteLock");
        this.readWriteLock = readWriteLock;
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
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.subList(fromIndex, toIndex);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.toArray();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] array) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.toArray(array);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        readWriteLock.writeLock().lock();
        try {
            return singlyLinkedList.toArray(generator);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        readWriteLock.writeLock().lock();
        try {
            return new SynchronizedSinglyLinkedList<T>(singlyLinkedList).listIterator();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        readWriteLock.writeLock().lock();
        try {
            var cList = new SynchronizedSinglyLinkedList<T>();
            var it = singlyLinkedList.listIterator(index);
            while (it.hasNext())
                cList.add(it.next());
            return cList.listIterator();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void throwIfNull(Object o, String parameterName) {
        if (o == null)
            throw new NullPointerException(parameterName);
    }
}
