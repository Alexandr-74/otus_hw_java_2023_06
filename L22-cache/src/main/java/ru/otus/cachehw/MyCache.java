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
        listeners.forEach(l -> Objects.requireNonNull(l.get()).notify(key, value, "Put"));
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        listeners.forEach(l -> Objects.requireNonNull(l.get()).notify(key, cache.get(key), "Remove"));
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        listeners.forEach(l -> Objects.requireNonNull(l.get()).notify(key, cache.get(key), "Find"));
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
