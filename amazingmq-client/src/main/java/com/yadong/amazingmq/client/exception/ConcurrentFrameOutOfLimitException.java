package com.yadong.amazingmq.client.exception;


/**
* @author YadongTan
* @date 2022/9/5 21:27
* @Description 当并发超出这个阈值时, 抛出此异常
*/
public class ConcurrentFrameOutOfLimitException extends RuntimeException {

    public ConcurrentFrameOutOfLimitException(String msg){
        super(msg);
    }

    public ConcurrentFrameOutOfLimitException(){
        super();
    }
}
