package ru.clevertec.normalino.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTests {

    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
    }

    @Test
    public void addToEmpty() {
        var item = "addToEmpty";
        list.add(item);
        assertEquals(item, list.get(0));
        assertEquals(list.size(), 1);
    }

    @Test
    public void addToNotEmpty() {

        Data.seed(list, 1);

        var existItem = list.get(0);
        var newItem = "addToNotEmpty";

        list.add(newItem);

        assertEquals(existItem, list.get(0));
        assertEquals(newItem, list.get(1));

        assertEquals(2, list.size());
    }

    @Test
    public void addByIndexToEnd() {

        Data.seed(list, 1);

        var existItem = list.get(0);
        var newItem = "addByIndexToEnd";

        list.add(list.size(), newItem);

        assertEquals(existItem, list.get(0));
        assertEquals(newItem, list.get(1));

        assertEquals(2, list.size());
    }

    @Test
    public void addByIndexToStart() {

        Data.seed(list, 1);

        var existItem = list.get(0);
        var newItem = "addByIndexToStart";

        list.add(0, newItem);

        assertEquals(existItem, list.get(1));
        assertEquals(newItem, list.get(0));

        assertEquals(2, list.size());
    }

    @Test
    public void addByIndexToCenter() {

        final var initialSize = 5;
        Data.seed(list, initialSize);

        var listCopy = new NormalinoList<>(list);
        var newItem = "addByIndexToCenter";

        var centerIndex = list.size() / 2; // 2

        list.add(centerIndex, newItem);

        for (int i = 0; i < centerIndex; i++) {
            assertEquals(list.get(i), listCopy.get(i));
        }

        assertEquals(newItem, list.get(centerIndex));

        for (int i = centerIndex + 1; i < list.size(); i++) {
            assertEquals(list.get(i), listCopy.get(i - centerIndex + 1));
        }

        assertEquals(initialSize + 1, list.size());
    }

    @Test
    public void addAll() {

        var initialListSize = 5;
        var subListSize = 10;
        var exceptedSize = initialListSize + subListSize;

        Data.seed(list, initialListSize);
        var listCopy = new NormalinoList<>(list);

        var subList = new NormalinoList<String>();
        Data.seed(subList, subListSize, "subListItem");

        list.addAll(subList);

        for (int i = 0; i < initialListSize; i++) {
            assertEquals(list.get(i), listCopy.get(i));
        }

        for (int i = initialListSize, j = 0; i < list.size(); i++, j++) {
            assertEquals(list.get(i), subList.get(j));
        }

        assertEquals(exceptedSize, list.size());
    }

    @Test
    public void addAllByIndexToStart() {

        final var initialListSize = 5;
        final int subListSize = 10;
        final var exceptedSize = initialListSize + subListSize;

        Data.seed(list, initialListSize);
        var listCopy = new NormalinoList<>(list);

        var subList = new NormalinoList<String>();
        Data.seed(subList, subListSize, "subListItem");

        list.addAll(0, subList);

        for (int i = 0; i < subListSize; i++) {
            assertEquals(list.get(i), subList.get(i));
        }

        for (int i = subListSize, j = 0; i < list.size(); i++, j++) {
            assertEquals(list.get(i), listCopy.get(j));
        }

        assertEquals(exceptedSize, list.size());
    }

    @Test
    public void addAllByIndexToEnd() {

        final var initialListSize = 5;
        final int subListSize = 10;
        final var exceptedSize = initialListSize + subListSize;

        Data.seed(list, initialListSize);
        var listCopy = new NormalinoList<>(list);

        var subList = new NormalinoList<String>();
        Data.seed(subList, subListSize, "subListItem");

        list.addAll(list.size(), subList);

        for (int i = 0; i < initialListSize; i++) {
            assertEquals(list.get(i), listCopy.get(i));
        }

        for (int i = initialListSize, j = 0; i < list.size(); i++, j++) {
            assertEquals(list.get(i), subList.get(j));
        }

        assertEquals(exceptedSize, list.size());
    }

    @Test
    public void addAllByIndexToCenter() {

        final var initialListSize = 5;
        final int subListSize = 10;
        final var exceptedSize = initialListSize + subListSize;

        Data.seed(list, initialListSize);
        var listCopy = new NormalinoList<>(list);

        var subList = new NormalinoList<String>();
        Data.seed(subList, subListSize, "subListItem");

        var centerIndex = list.size() / 2;

        list.addAll(centerIndex, subList);

        for (int i = 0; i < centerIndex; i++) {
            assertEquals(list.get(i), listCopy.get(i));
        }

        for (int i = centerIndex, j = 0; i < subListSize + centerIndex; i++, j++) {
            assertEquals(list.get(i), subList.get(j));
        }

        for (int i = subListSize + centerIndex, j = centerIndex; i < list.size(); i++, j++) {
            assertEquals(list.get(i), listCopy.get(j));
        }

        assertEquals(exceptedSize, list.size());
    }
}
