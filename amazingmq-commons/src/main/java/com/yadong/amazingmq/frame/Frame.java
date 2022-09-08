package com.yadong.amazingmq.frame;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author YadongTan
* @date 2022/9/5 8:10
* @Description
*/
public class Frame {
    private static final AtomicInteger atomicId = new AtomicInteger(0);

    private int frameId;    //帧id

    private short type;

    private short channelId;

    private int size = 0;       //默认是0
    private String payload;

    private char frameEnd = FRAME_END; //默认就有

    public static final char FRAME_END = '\r';

    public enum PayloadType {
        ERROR((short) -1),    //错误帧
        CREATE_CONNECTION((short) 1), //创建连接Connection
        CREATE_CHANNEL((short) 2),   //在连接内创建一个Channel
        EXCHANGE_DECLARED((short)3), // 声明一个交换机
        QUEUE_DECLARED((short)4),   //声明一个队列
        BINDING_DECLARED((short)5), //声明绑定
        CREATE_ID_MAX((short)16),   //创建连接相关的最大值
        SUCCESSFUL((short)17);    //创建组件成功
        short type;

        PayloadType(short type) {
            this.type = type;
        }

        public short getType() {
            return type;
        }

        public void setType(short type) {
            this.type = type;
        }
    }

    public Frame(short type, short channelId, int size, String payload, char frameEnd) {
        this.type = new Short(type).byteValue();
        this.channelId = channelId;
        this.size = size;
        this.payload = payload;
        this.frameEnd = frameEnd;
        this.frameId = atomicId.getAndIncrement();
    }

    public Frame(int frameId, short type, short channel, int size, String payload, char frameEnd) {
        this.frameId = frameId;
        this.type = new Short(type).byteValue();
        this.channelId = channel;
        this.size = size;
        this.payload = payload;
        this.frameEnd = frameEnd;
    }

    public Frame() {
        this.frameId = atomicId.getAndIncrement();
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getChannelId() {
        return channelId;
    }

    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
        this.size = payload.getBytes(StandardCharsets.UTF_8).length;
    }

    public char getFrameEnd() {
        return frameEnd;
    }

    public void setFrameEnd(char frameEnd) {
        this.frameEnd = frameEnd;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "frameId=" + frameId +
                ", type=" + type +
                ", channelId=" + channelId +
                ", size=" + size +
                ", payload='" + payload + '\'' +
                '}';
    }
}