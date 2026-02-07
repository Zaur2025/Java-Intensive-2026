package io.github.zaur2025.myhashmap;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        System.out.println("Тест собственной реализации MyHashMap:\n");

        // 1. Добавление пар ключ-значение
        map.put("Ivan", 21);
        map.put("Anna", 22);
        System.out.println("1. Добавление пар ключ-значение:");
        System.out.println("   put(\"Ivan\", 21) -> get(\"Ivan\") = " + map.get("Ivan"));
        System.out.println("   put(\"Anna\", 22) -> get(\"Anna\") = " + map.get("Anna"));

        // 2. Обновление значения по существующему ключу
        map.put("Ivan", 30);
        System.out.println("\n2. Обновление значения по ключу:");
        System.out.println("   put(\"Ivan\", 30) -> get(\"Ivan\") = " + map.get("Ivan"));

        // 3. Удаление пары по ключу
        Integer removed = map.remove("Anna");
        System.out.println("\n3. Удаление пары по ключу:");
        System.out.println("   remove(\"Anna\") вернул: " + removed);
        System.out.println("   get(\"Anna\") после удаления: " + map.get("Anna"));

        // 4. Пример коллизии
        System.out.println("\n4. Пример коллизий:");
        System.out.println("   Хэш 'Aa' = " + "Aa".hashCode());
        System.out.println("   Хэш 'BB' = " + "BB".hashCode());
        System.out.println("   Одинаковый хэш -> попадут в одну ячейку (коллизия)");

        map.put("Aa", 100);
        map.put("BB", 200);
        System.out.println("   put(\"Aa\", 100) -> get(\"Aa\") = " + map.get("Aa"));
        System.out.println("   put(\"BB\", 200) -> get(\"BB\") = " + map.get("BB"));
        System.out.println("   Обе пары доступны несмотря на коллизию благодаря цепочкам (chaining)");

        // 5. Дополнительные проверки
        System.out.println("\n5. Дополнительные проверки:");
        System.out.println("   Размер HashMap: " + map.size());
        System.out.println("   Пуст ли HashMap: " + map.isEmpty());
        System.out.println("   Содержит ключ \"Aa\": " + (map.get("Aa") != null));
        System.out.println("   Содержит ключ \"Несуществующий\": " + (map.get("Несуществующий") != null));
        System.out.println();
        System.out.println("Спасибо за внимание!");
    }
}