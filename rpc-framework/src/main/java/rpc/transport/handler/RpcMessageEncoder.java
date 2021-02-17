package rpc.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.transport.dto.RpcMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext CTX, RpcMessage rpcMessage, ByteBuf out) {

    }
}
