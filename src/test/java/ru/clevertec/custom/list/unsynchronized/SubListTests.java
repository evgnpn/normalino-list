package ru.clevertec.custom.list.unsynchronized;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.custom.list.Data;
import ru.clevertec.custom.list.SinglyLinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubListTests {

    private SinglyLinkedList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SinglyLinkedList<>();
        Data.seed(list, 20);
    }

    @Test
    public void subListFromStartToCenter() {

        var centerIndex = list.size() / 2;

        var subList = list.subList(0, centerIndex);

        assertEquals(centerIndex, subList.size());

        for (int i = 0; i < centerIndex; i++) {
            assertEquals(list.get(i), subList.get(i));
        }
    }

    @Test
    public void subListFromCenterToEnd() {

        var centerIndex = list.size() / 2;

        var subList = list.subList(centerIndex, list.size());

        for (int i = centerIndex, j = 0; i < list.size(); i++, j++) {
            assertEquals(list.get(i), subList.get(j));
        }
    }

    @Test
    public void subListFromAfterFirstToBeforeLast() {

        var afterFirstIndex = 1;
        var beforeLastIndex = list.size() - 2;

        var subList = list.subList(afterFirstIndex, beforeLastIndex + 1);

        for (int i = 1, j = 0; i <= beforeLastIndex; i++, j++) {
            assertEquals(list.get(i), subList.get(j));
        }
    }
}
