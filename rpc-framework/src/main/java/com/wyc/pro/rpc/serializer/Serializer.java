package com.wyc.pro.rpc.serializer;

import com.wyc.pro.rpc.extension.SPI;

@SPI
public interface Serializer {
    byte[] serialize(Object obj);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
