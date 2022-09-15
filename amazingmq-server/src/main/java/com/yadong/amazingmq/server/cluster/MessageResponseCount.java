package com.yadong.amazingmq.server.cluster;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageResponseCount {
    private String queueName;
    private AtomicInteger count;
    private boolean ok = false;

    public MessageResponseCount(int count){
        this.count = new AtomicInteger(count);
    }

    public void messageRemoved(){
        int i = count.decrementAndGet();
        if (i == 0) {
            ok = true;
            synchronized (this){
                this.notify();
            }
        }
    }

    /**
    * @author YadongTan
    * @date 2022/9/15 17:15
    * @Description 当发现请求的消息已经被发送了的时候, 调用这个函数
    */
    public void alreadySent(){
        ok = false;
        synchronized (this){
            this.notify();
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
