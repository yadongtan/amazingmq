package com.yadong.amazingmq.server.vhost;

public class IncorrectPasswordException extends AuthErrorException {

    IncorrectPasswordException(String msg){
        super(msg);
    }
}
