package dk.sebsa.coal.io.util;

import java.util.HashMap;
import java.util.Map;

public class FourKeyHashMap<K1, K2, K3, K4, V> {
    private final Map<K1, Map<K2, Map<K3, Map<K4, V>>>> theBigMotherFuckerMap;

    public FourKeyHashMap() {
        theBigMotherFuckerMap = new HashMap<>();
    }

    public V get(K1 key1, K2 key2, K3 key3, K4 key4) {
        try {
            return theBigMotherFuckerMap.get(key1).get(key2).get(key3).get(key4);
        } catch (NullPointerException e) { return null; }
    }

    public V getPut(K1 key1, K2 key2, K3 key3, K4 key4, V v) {
        V r = get(key1, key2, key3, key4);
        if (r == null) put(key1,key2,key3,key4,v);
        return r == null ? v : r;
    }

    public void put(K1 key1, K2 key2, K3 key3, K4 key4, V v) {
        var map1 = theBigMotherFuckerMap.computeIfAbsent(key1, k -> new HashMap<>());
        var map2 = map1.computeIfAbsent(key2, k -> new HashMap<>());
        var map3 = map2.computeIfAbsent(key3, k -> new HashMap<>());
        map3.put(key4, v);
    }
}
