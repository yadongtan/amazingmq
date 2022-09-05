package com.yadong.amazingmq.frame;

import java.nio.charset.StandardCharsets;

/**
* @author YadongTan
* @date 2022/9/5 8:10
* @Description
*/
public class Frame {

    private short type;

    private short channel;

    private int size;
    private String payload;

    private char frameEnd;

        public static final char FRAME_END = '\r';

        public enum PayloadType{
            CREATE_CONNECTION((short) 1), //创建连接Connection
            CREATE_CHANNEL((short)2);   //在连接内创建一个Channel
            short type;
            PayloadType(short type){
                this.type = type;
            }

            public short getType() {
                return type;
            }

            public void setType(short type) {
                this.type = type;
            }
        }

    public Frame(short type, short channel, int size, String payload, char frameEnd) {
            this.type = new Short(type).byteValue();
        this.channel = channel;
        this.size = size;
        this.payload = payload;
        this.frameEnd = frameEnd;
    }

    public Frame(){

    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getChannel() {
        return channel;
    }

    public void setChannel(short channel) {
        this.channel = channel;
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
    }

    public char getFrameEnd() {
        return frameEnd;
    }

    public void setFrameEnd(char frameEnd) {
        this.frameEnd = frameEnd;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "type=" + type +
                ", channel=" + channel +
                ", size=" + size +
                ", payload='" + payload + '\'' +
                "'}'";
    }
}
