package com.yadong.amazingmq.server.vhost;


import com.yadong.amazingmq.server.connection.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
* @author YadongTan
* @date 2022/9/6 10:52
* @Description 管理自己的Connection
*/
public class VirtualHost {

    private String path;
    // 连接id 和 Connection
    private ConcurrentHashMap<Short, Connection> connectionMap
            = new ConcurrentHashMap<>();

    public VirtualHost(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addConnection(Connection connection){
        connectionMap.put(connection.getConnectionId(), connection);
    }

    public void removeConnection(short cid){
        connectionMap.remove(cid);
    }

}
