package com.yadong.amazingmq.server.vhost;

public class VhostDeniedException extends AuthErrorException {

    VhostDeniedException(String msg){
        super(msg);
    }

}
