package com.yadong.amazingmq.frame;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Delayed {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int messageId;
    private byte[] content;
    private long start = 0;

    public Message(){}

    public Message(byte[] content, long delayInMilliseconds){
        this.messageId = idGenerator.getAndIncrement();
        this.content = content;
        this.start = System.currentTimeMillis() + delayInMilliseconds;
    }
    public Message(byte[] content){
        this.messageId = idGenerator.getAndIncrement();
        this.content = content;
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

}
