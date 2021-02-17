package common;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new HashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance;
        synchronized (SingletonFactory.class) {
            instance = OBJECT_MAP.get(key);
            if(instance == null) {
                try {
                    instance = c.getDeclaredConstructor().newInstance();
                    OBJECT_MAP.put(key, instance);
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return c.cast(instance);
    }
}
