package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;


public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        try {
            listeners.forEach(l -> Objects.requireNonNull(l.get()).notify(key, value, "Put"));
        } catch (Exception e) {
            System.out.println("Ошибка при оповещении слушателя: " + e.getMessage());
        }
            cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        try {
            listeners.forEach(l -> {
                try {
                    Objects.requireNonNull(l.get()).notify(key, cache.get(key), "Remove");
                } catch (NullPointerException ex) {
                    listeners.remove(l);
                }
            });
        }  catch (Exception e) {
            System.out.println("Ошибка при оповещении слушателя: " + e.getMessage());
        }
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        try {
            listeners.forEach(l -> Objects.requireNonNull(l.get()).notify(key, cache.get(key), "Find"));
        } catch (Exception e) {
            System.out.println("Ошибка при оповещении слушателя: " + e.getMessage());
        }
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        WeakReference<HwListener<K, V>> hwListenerWeakReference = new WeakReference<>(listener);
        listeners.add(hwListenerWeakReference);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listeners.stream()
                .filter(o -> Objects.equals(o.get(), listener))
                .findAny()
                .orElseThrow());
    }
}
