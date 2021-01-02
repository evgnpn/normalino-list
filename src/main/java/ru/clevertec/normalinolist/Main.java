package ru.clevertec.normalinolist;

public class Main {
    public static void main(String[] args) {

        var subList1 = new NormalinoList<String>();
        subList1.add("subList1_1");
        subList1.add("subList1_2");
        subList1.add("subList1_3");
        subList1.add("subList1_4");

        var subList2 = new NormalinoList<String>();
        subList2.add("subList2_1");
        subList2.add("subList2_2");
        subList2.add("subList2_3");
        subList2.add("subList2_4");
        subList2.add("subList2_5");

        var list = new NormalinoList<String>();

        list.addAll(subList1);
        list.add("hello1a");
        list.add("hello2a");
        list.add("hello3a");
        list.add("hello4a");
        list.addAll(subList2);
        list.add("hello5a");
        list.add("hello6a");
        list.add(list.size(), "addedToEndByIndex");
        list.add(list.size() / 2, "addedToCenterByIndex");
        list.add(0, "addedToStartByIndex");

        for (var item : list) {
            System.out.println(" - " + item);
        }

        System.out.println("\nSubList (2-7):");

        for (var item : list.subList(2, 7)) {
            System.out.println(" - " + item);
        }

        System.out.println("\nRemoved subList1:");

        list.removeAll(subList1);

        for (var item : list) {
            System.out.println(" - " + item);
        }

        System.out.println("\nAccess by index: ");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(" - i:" + i + " = " + list.get(i));
        }
    }
}
