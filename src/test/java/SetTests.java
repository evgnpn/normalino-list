import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.normalinolist.NormalinoList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetTests {

    private NormalinoList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new NormalinoList<>();
    }

    @Test
    public void set() {

        final var initialSize = 3;

        Data.seed(list, initialSize);

        for (int i = 0; i < list.size(); i++) {
            list.set(i, "set" + i);
        }

        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), "set" + i);
        }

        assertEquals(list.size(), initialSize);
    }

    @Test
    public void setIndexMoreThanSizeThrows() {

        final var initialSize = 3;

        Data.seed(list, initialSize);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(initialSize, "setToEmptyThrows"));
    }

    @Test
    public void setToEmptyThrows() {

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(0, "setToEmptyThrows"));
    }
}
