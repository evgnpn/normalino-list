package ru.clevertec.normalino.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RetainAllTests {

    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
        Data.seed(list, 20);
    }

    @Test
    public void retainAll() {

        var itemsToRetain = IntStream.range(0, list.size()).filter(i -> i % 2 == 0)
                .mapToObj(i -> list.get(i)).collect(Collectors.toList());

        var retained = list.retainAll(itemsToRetain);

        assertTrue(retained);

        for (var item : list) {
            assertTrue(itemsToRetain.contains(item));
        }
    }
}
