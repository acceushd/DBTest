import java.util.HashMap;
import java.util.Map;

public class TypedDynamicMap<K, V> {
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final Map<K, V> map;

    public TypedDynamicMap(Class<K> keyType, Class<V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.map = new HashMap<>();
    }

    public void put(K key, V value) {
        if (!keyType.isInstance(key)) {
            throw new IllegalArgumentException("Key is not an instance of " + keyType);
        }
        if (!valueType.isInstance(value)) {
            throw new IllegalArgumentException("Value is not an instance of " + valueType);
        }
        map.put(key, value);
    }

    public V get(K key) {
        if (!keyType.isInstance(key)) {
            throw new IllegalArgumentException("Key is not an instance of " + keyType);
        }
        return map.get(key);
    }
}