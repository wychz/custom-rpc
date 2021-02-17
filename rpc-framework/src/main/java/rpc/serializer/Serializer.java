package rpc.serializer;

import rpc.extension.SPI;

@SPI
public interface Serializer {
    byte[] serialize(Object obj);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
