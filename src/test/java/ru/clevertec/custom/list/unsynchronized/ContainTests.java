package ru.clevertec.custom.list.unsynchronized;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.custom.list.Data;
import ru.clevertec.custom.list.SinglyLinkedList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainTests {

    private SinglyLinkedList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SinglyLinkedList<>();
        Data.seed(list, 10);
    }

    @Test
    public void contains() {
        assertTrue(list.contains(list.get(4)));
    }

    @Test
    public void containsNegative() {
        assertFalse(list.contains("containsNegative"));
    }

    @Test
    public void containsAll() {
        var elements = IntStream.range(0, list.size()).filter(i -> i % 2 == 0)
                .mapToObj(i -> list.get(i)).collect(Collectors.toList());

        assertTrue(list.containsAll(elements));
    }

    @Test
    public void containsAllNegative() {
        var elements = IntStream.range(0, list.size()).filter(i -> i % 2 == 0)
                .mapToObj(i -> list.get(i)).collect(Collectors.toList());
        elements.add("containsAllNegative");

        assertFalse(list.containsAll(elements));
    }
}
