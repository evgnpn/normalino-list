import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.normalinolist.NormalinoList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexTests {
    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
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
