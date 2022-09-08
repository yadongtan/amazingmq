package com.yadong.amazingmq.server.queue;

import java.io.IOException;

public class OutOfMaxLengthException extends IOException {


    public OutOfMaxLengthException(String msg){
        super(msg);
    }
}
