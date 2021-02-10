package rpc.extension;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ExtensionLoader<T> {
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();  // 该类类型 -》 Loader
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();     // 类型 -》实例

    private final Class<?> type;
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();      // 名称 -》 Holder（保存实例Instance）
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();     // 名称 -》 类型

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    //获取类型对应的ExtensionLoader
    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        if(type == null) {
            throw new IllegalArgumentException("Extension type should not be null");
        }
        if(!type.isInterface()) {
            throw new IllegalArgumentException("Extension type should be a interface");
        }
        if(type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type should be annotated by @SPI");
        }
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        if(extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    // holder中保存的是扩展的实例对象instance
    // 返回一个实例对象类名对应的实例对象
    public T getExtension(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name should not be null or empty");
        }
        Holder<Object> holder = cachedInstances.get(name);
        if(holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if(instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if(instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T)instance;
    }

    // 根据扩展名 -》扩展类型 -》获得一个实例对象并返回
    // 获取的是文件内部类型的实例对象
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if(clazz == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if(instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return instance;
    }

    //返回一个文件内部里的 扩展名-》扩展类型 的 Map。 并存入Holder对象cachedClasses
    //Holder对象cachedClasses保存的是文件内容的map
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if(classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if(classes == null) {
                    classes = new HashMap<>();
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    //加载META/extensions目录下，该ExtensionLoader的扩展
    //extensionClasses保存的是文件内容里的 name -> 实际类型
    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getSimpleName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if(urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    //加载META/extensions下目录下该type的扩展，并放入extensionClasses
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
            String line;
            while((line = reader.readLine()) != null) {
                final int ci = line.indexOf('#');
                if(ci >= 0) {
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if(line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String clazzName = line.substring(ei + 1).trim();
                        if(name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
