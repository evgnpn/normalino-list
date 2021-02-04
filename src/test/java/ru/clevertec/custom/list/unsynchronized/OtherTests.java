package ru.clevertec.custom.list.unsynchronized;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.custom.list.Data;
import ru.clevertec.custom.list.SinglyLinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OtherTests {

    private final int initialSize = 10;

    private SinglyLinkedList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SinglyLinkedList<>();
    }

    @Test
    public void isEmpty() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void clear() {
        Data.seed(list, initialSize);
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    public void size() {
        Data.seed(list, initialSize);
        assertEquals(initialSize, list.size());
    }
}
