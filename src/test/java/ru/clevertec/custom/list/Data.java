package ru.clevertec.custom.list;

import java.util.List;

public class Data {
    public static void seed(List<String> list, int count) {
        seed(list, count, "e");
    }

    public static void seed(List<String> list, int count, String prefix) {
        for (int i = 0; i < count; i++)
            list.add(prefix + i);
    }
}
