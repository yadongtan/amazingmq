package com.yadong.amazingmq.server.vhost;

import java.io.IOError;
import java.io.IOException;

public class AuthErrorException extends IOException {

    AuthErrorException(String msg){
        super(msg);
    }
}
