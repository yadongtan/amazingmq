package com.yadong.amazingmq.codec;


import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
* @author YadongTan
* @date 2022/9/5 8:40
* @Description 编码器,遵循Amqp-0-9-1协议消息格式
 * 0-4字节 frameId -- 自己加的
 * 4-5字节 type octet
 * 5-7字节 channel short
 * 7-11字节 size long
 * 11 - size+11字节 payload size octets
 * size+11 - size+12字节 frame-end octet
*/
public class BrokerNettyEncoder extends MessageToByteEncoder<Frame> {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyEncoder.class);
    public static final String DELIMITER = "\r\t";

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) throws Exception {

        ByteBuffer outBytes = ByteBuffer.allocate(msg.getSize() + 14);

        byte[] frameIdBytes = NettyUtils.intToBytes(msg.getFrameId());
        outBytes.put(frameIdBytes);

        byte typeBytes = (byte) (msg.getType() & 0xFF);
        outBytes.put(typeBytes);

        byte[] channelBytes = new byte[2];
        channelBytes[0] = (byte) ((msg.getChannelId() & 0xFF00) >> 8);
        channelBytes[1] = (byte) (msg.getChannelId() & 0xFF);
        outBytes.put(channelBytes);

        byte[] sizeBytes = new byte[4];
        byte start = (byte) 0XFF;
        int size = msg.getSize();
        // 10 9 8 7
        // 3 2 1 0
        for (int i = 10; i >= 7; i--) {
            sizeBytes[i - 7] = (byte) (size & start);
            size = size >> 8;
        }
        outBytes.put(sizeBytes);
        if(msg.getSize() != 0) {
            byte[] payloadBytes = msg.getPayload().getBytes(StandardCharsets.UTF_8);
            outBytes.put(payloadBytes);
        }
        byte frameEndBytes = (byte )(msg.getFrameEnd() & 0xFF);
        outBytes.put(frameEndBytes);
        outBytes.put(DELIMITER.getBytes());
        out.writeBytes(outBytes.array());
    }



}