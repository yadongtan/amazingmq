package com.yadong.amazingmq.codec;


import com.yadong.amazingmq.frame.Frame;
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
 * 0-1字节 type octet
 * 1-3字节 channel short
 * 3-7字节 size long
 * 7-size+7字节 payload size octets
 * size+7-size+8字节 frame-end octet
*/
public class BrokerNettyEncoder extends MessageToByteEncoder<Frame> {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyEncoder.class);
    public static final String DELIMITER = "\r\t";

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) throws Exception {

        ByteBuffer outBytes = ByteBuffer.allocate(msg.getSize() + 10);

        byte typeBytes = (byte) (msg.getType() & 0xFF);
        outBytes.put(typeBytes);

        byte[] channelBytes = new byte[2];
        channelBytes[0] = (byte) ((msg.getChannel() & 0xFF00) >> 8);
        channelBytes[1] = (byte) (msg.getChannel() & 0xFF);
        outBytes.put(channelBytes);

        byte[] sizeBytes = new byte[4];
        byte start = (byte) 0XFF;
        int size = msg.getSize();
        for (int i = 3; i >= 0; i--) {
            sizeBytes[i] = (byte) (size & start);
            size = size >> 8;
        }
        outBytes.put(sizeBytes);

        byte[] payloadBytes = msg.getPayload().getBytes(StandardCharsets.UTF_8);
        outBytes.put(payloadBytes);

        byte frameEndBytes = (byte )(msg.getFrameEnd() & 0xFF);
        outBytes.put(frameEndBytes);
        outBytes.put(DELIMITER.getBytes());
        out.writeBytes(outBytes.array());
    }



}