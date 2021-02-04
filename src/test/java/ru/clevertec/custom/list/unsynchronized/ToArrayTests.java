package ru.clevertec.custom.list.unsynchronized;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.custom.list.Data;
import ru.clevertec.custom.list.SinglyLinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToArrayTests {
    private SinglyLinkedList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SinglyLinkedList<>();
        Data.seed(list, 20);
    }

    @Test
    public void toArray() {

        var arr = list.toArray();

        assertEquals(arr.length, list.size());

        for (int i = 0; i < arr.length; i++) {
            assertEquals(list.get(i), arr[i]);
        }
    }

    @Test
    public void toArrayWithExistArray() {

        var eArr = new String[list.size()];
        var arr = list.toArray(eArr);

        assertEquals(arr.length, list.size());

        for (int i = 0; i < arr.length; i++) {
            assertEquals(list.get(i), arr[i]);
        }
    }
}
