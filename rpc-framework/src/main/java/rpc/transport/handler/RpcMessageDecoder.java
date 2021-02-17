package rpc.transport.handler;

import common.enums.CompressTypeEnum;
import common.enums.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import rpc.Compress.Compress;
import rpc.extension.ExtensionLoader;
import rpc.serializer.Serializer;
import rpc.transport.constants.RpcConstants;
import rpc.transport.dto.RpcMessage;
import rpc.transport.dto.RpcRequest;
import rpc.transport.dto.RpcResponse;

import java.util.Arrays;


//ByteBuf https://blog.csdn.net/linuu/article/details/51322224
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if(decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if(frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        byte codeType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder().codec(codeType).requestId(requestId).messageType(messageType).build();
        if(messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if(messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if(bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            in.readBytes(bs);
            String compressName = CompressTypeEnum.getName(compressType);
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
            bs = compress.decompress(bs);
            String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
            log.info("codec name: [{}]", codecName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
            if(messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest tmpValue = serializer.deserialize(bs, RpcRequest.class);
                rpcMessage.setData(tmpValue);
            } else {
                RpcResponse tmpValue = serializer.deserialize(bs, RpcResponse.class);
                rpcMessage.setData(tmpValue);
            }
        }
        return rpcMessage;
    }

    private void checkMagicNumber(ByteBuf in) {
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for(int i = 0; i < len; i++) {
            if(tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if(version != RpcConstants.VERSION) {
            throw new RuntimeException("version is not compatible" + version);
        }
    }
}
