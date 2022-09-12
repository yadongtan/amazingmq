package com.yadong.amazingmq.frame;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Delayed {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int messageId;
    private byte[] content;
    private long start = 0; //消息什么时候才应该被发送,仅当这是一条延迟消息的时候才启用
    private long createTime;    //消息的创建时间
    private long x_message_ttl; //消息过期时间,单位 ms毫秒

    public Message(){}

    public Message(byte[] content, long delayInMilliseconds, long x_message_ttl){
        this(content, delayInMilliseconds);
        this.x_message_ttl = x_message_ttl;
    }

    public Message(byte[] content, long delayInMilliseconds){
        this.messageId = idGenerator.getAndIncrement();
        this.content = content;
        this.createTime = System.currentTimeMillis();
        this.start = System.currentTimeMillis() + delayInMilliseconds;
    }

    public Message(byte[] content){
        this.messageId = idGenerator.getAndIncrement();
        this.content = content;
        this.createTime = System.currentTimeMillis();
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = this.start - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    // 排序
    @Override
    public int compareTo(Delayed o) {
        Message e = (Message) o;
        long deff = this.start - e.start;
        if (deff <= 0) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Message)) {
            return false;
        }
        Message t = (Message)o;
        return this.messageId == t.getMessageId();
    }

    @Override
    public int hashCode() {
        return this.messageId;
    }

    public long getX_message_ttl() {
        return x_message_ttl;
    }

    public void setX_message_ttl(long x_message_ttl) {
        this.x_message_ttl = x_message_ttl;
    }
}
