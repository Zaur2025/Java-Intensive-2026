package io.github.zaur2025.myhashmap;

public class MyHashMap<K, V> {

    // Внутренний класс для хранения пары ключ-значение
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next; // для коллизий

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Массив для хранения цепочек
    private Entry<K, V>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    // Конструктор
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    // Метод put добавляет или обновляет значение по ключу
    public void put(K key, V value) {
        if (key == null) return;

        int index = getIndex(key);
        Entry<K, V> current = table[index];

        // Проверяем, есть ли уже такой ключ
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value; // Обновляем значение
                return;
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    // Метод get возвращает значение по ключу
    public V get(K key) {
        if (key == null) return null;

        int index = getIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null; // Ключ не найден
    }

    // Метод remove удаляет пару по ключу
    public V remove(K key) {
        if (key == null) return null;

        int index = getIndex(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    // Удаляем первый элемент цепочки
                    table[index] = current.next;
                } else {
                    // Удаляем из середины или конца
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null; // Ключ не найден
    }

    // Вспомогательный метод для вычисления индекса
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    // Метод для вычисления размера
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}