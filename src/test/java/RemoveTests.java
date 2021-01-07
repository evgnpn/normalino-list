import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.normalinolist.NormalinoList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveTests {

    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
        Data.seed(list, 10);
    }

    @Test
    public void removeByObjectFromStart() {
        removeByObject(0);
    }

    @Test
    public void removeByObjectFromEnd() {
        removeByObject(list.size() - 1);
    }

    @Test
    public void removeByObjectFromCenter() {
        removeByObject(list.size() / 2);
    }

    @Test
    public void removeByIndexFromStart() {
        removeByIndex(0);
    }

    @Test
    public void removeByIndexFromEnd() {
        removeByIndex(list.size() - 1);
    }

    @Test
    public void removeByIndexFromCenter() {
        removeByIndex(list.size() / 2);
    }

    @Test
    public void removeAll() {

        var toRemoveItems = IntStream.range(0, list.size()).filter(i -> i % 2 == 0)
                .mapToObj(i -> list.get(i)).collect(Collectors.toList());

        list.removeAll(toRemoveItems);

        for (var item : toRemoveItems) {
            assertFalse(list.contains(item));
        }
    }

    private void removeByObject(int objIndex) {

        var srcSize = list.size();
        var object = list.get(objIndex);
        var removed = list.remove(object);

        assertTrue(removed);
        assertFalse(list.contains(object));

        assertEquals(list.size() + 1, srcSize);
    }

    private void removeByIndex(int index) {

        var srcSize = list.size();
        var toRemoveObj = list.get(index);
        var removedObj = list.remove(index);

        assertEquals(toRemoveObj, removedObj);
        assertFalse(list.contains(toRemoveObj));

        assertEquals(list.size() + 1, srcSize);
    }
}
