package com.yadong.amazingmq.proxy.loadbalance;

public class UnknownLoadBalanceException extends RuntimeException {
    public UnknownLoadBalanceException(String msg){
        super(msg);
    }
}
