package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    WeakHashMap<K,V> weakHashMap = new WeakHashMap<>();
    List<WeakReference<HwListener<K, V>>> weakReferences = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        weakHashMap.put(key,value);
    }

    @Override
    public void remove(K key) {
        weakHashMap.remove(key);
    }

    @Override
    public V get(K key) {
        return weakHashMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        WeakReference<HwListener<K, V>> hwListenerWeakReference = new WeakReference<>(listener);
        weakReferences.add(hwListenerWeakReference);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        weakReferences.remove(weakReferences.stream().filter(o-> Objects.equals(o.get(), listener)).findAny().orElseThrow());
    }
}
