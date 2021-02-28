package com.wyc.pro.rpc.compress;

import com.wyc.pro.rpc.extension.SPI;

@SPI
public interface Compress {
    byte[] compress(byte[] bytes);
    byte[] decompress(byte[] bytes);
}
