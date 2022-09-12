package com.yadong.amazingmq.server.exchange;


public class NoSuchExchangeException extends RuntimeException {

    public NoSuchExchangeException(String msg){
        super(msg);
    }
}
