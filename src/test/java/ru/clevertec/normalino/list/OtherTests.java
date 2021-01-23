package ru.clevertec.normalino.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OtherTests {

    private final int initialSize = 10;

    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
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
