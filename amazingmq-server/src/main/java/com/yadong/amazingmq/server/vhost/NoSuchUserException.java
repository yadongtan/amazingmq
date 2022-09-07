package com.yadong.amazingmq.server.vhost;

public class NoSuchUserException extends AuthErrorException {

    NoSuchUserException(String msg){
        super(msg);
    }
}
