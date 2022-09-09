package com.yadong.amazingmq.codec;


import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;

public class BrokerNettyDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyDecoder.class);

    /**
     * decode() 会根据接收的数据，被调用多次，知道确定没有新的元素添加到list,
     * 或者是 ByteBuf 没有更多的可读字节为止。
     * 如果 list 不为空，就会将 list 的内容传递给下一个 handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data, in.readerIndex(), in.readableBytes());

        Base64.Decoder decoder = Base64.getDecoder();
        data = decoder.decode(data);    //Base64解码

        in = Unpooled.copiedBuffer(data);

        int frameId = in.readInt();
        short type = in.readByte();

        short channel = in.readShort();
        int size = in.readInt();

        String payloadStr = null;
        if (size != 0) {
            byte[] payload = new byte[size];
            try {
                in.readBytes(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
            payloadStr = new String(payload);
        }
        char frameEnd = (char) in.readByte();
        Frame frame = new Frame(frameId, type, channel, size, payloadStr, frameEnd);
        out.add(frame);
    }

}