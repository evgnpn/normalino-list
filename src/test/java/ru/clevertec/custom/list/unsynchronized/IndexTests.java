package ru.clevertec.custom.list.unsynchronized;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.custom.list.Data;
import ru.clevertec.custom.list.SinglyLinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class IndexTests {
    private SinglyLinkedList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SinglyLinkedList<>();
        Data.seed(list, 10);
    }

    @Test
    public void getByIndex() {
        for (int i = 0; i < list.size(); i++) {
            var item = list.get(i);
            assertFalse(item == null || item.isEmpty());
        }
    }

    @Test
    public void getByIndexLessThanZeroThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    public void getByIndexMoreThanSizeThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
    }

    @Test
    public void indexOf() {

        var exceptedIndex = 8;
        var item = list.get(exceptedIndex);
        var actualIndex = list.indexOf(item);

        assertEquals(exceptedIndex, actualIndex);
    }

    @Test
    public void indexOfNotExist() {

        var item = "indexOfNotExist";
        var actualIndex = list.indexOf(item);

        assertEquals(-1, actualIndex);
    }

    @Test
    public void lastIndexOf() {

        var exceptedIndex = 8;
        var item = list.get(exceptedIndex);
        list.add(2, item);

        var actualIndex = list.lastIndexOf(item);

        assertEquals(exceptedIndex + 1, actualIndex);
    }

    @Test
    public void lastIndexOfNotExist() {

        var item = "lastIndexOfNotExist";
        var actualIndex = list.lastIndexOf(item);

        assertEquals(-1, actualIndex);
    }
}
