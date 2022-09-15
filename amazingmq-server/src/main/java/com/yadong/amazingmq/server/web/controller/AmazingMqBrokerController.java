package com.yadong.amazingmq.server.web.controller;

import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.property.BrokerProperties;
import com.yadong.amazingmq.server.property.UserProperties;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AmazingMqBrokerController {


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexPage(){
        StringBuilder builder = new StringBuilder();
        ConcurrentHashMap<String, UserProperties> userPropertiesMap =
                AmazingMqBroker.getInstance().getUserPropertiesMap();

        builder.append("<br/>用户信息:");
        userPropertiesMap.forEach((k, v) -> {

            builder.append("<br/>-------------------------------------------------")
                    .append("<br/>&nbsp&nbsp&nbsp&nbsp 用户名:" + v.getUsername())
                    .append("<br/>&nbsp&nbsp&nbsp&nbsp 密码:" + v.getPassword())
                    .append("<br/>&nbsp&nbsp&nbsp&nbsp vhost:" + v.getAllVhost());
        });

        builder.append("<br/><br/><br/>===============================================");
        builder.append("<br/>vhost 连接信息:");
        ConcurrentHashMap<String, VirtualHost> virtualHostMap =
                AmazingMqBroker.getInstance().getVirtualHostMap();
        virtualHostMap.forEach((path, vhost) ->{
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp path:" + vhost.getPath());
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp Connection:");
            vhost.getConnectionMap().forEach((id, connection)->{
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp id: " + connection.getConnectionId());
                connection.getChannelMap().forEach((channelId, channel) ->{
                    builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
                            + " channelId: "+ channel.getChannelId());
                });
            });
        });

        builder.append("<br/><br/><br/>===============================================");
        builder.append("<br/> 消息队列信息: ");
        AmazingMqBroker.getInstance().getVirtualHostMap().forEach((path, vhost)->{
            vhost.getExchangeMap().forEach((exchangeName, exchange)->{
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp交换机name:" +exchangeName);
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp:" + ObjectMapperUtils.toJSON(exchange));
                builder.append("<br/>-------------------------------------------------");
            });
            vhost.getQueueMap().forEach((ququeName, queue)->{
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp队列name:" +ququeName);
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp:" + ObjectMapperUtils.toJSON(queue));
                builder.append("<br/>-------------------------------------------------");
            });
            vhost.getBindingMap().forEach((bindingName, binding)->{
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp绑定name:" +bindingName);
                builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp:" + ObjectMapperUtils.toJSON(binding));
                builder.append("<br/>-------------------------------------------------");
            });
        });
        builder.append("<br/>==========================================================");
        StringBuilder connectionInfo = new StringBuilder();
        connectionInfo.append("<br/><br/>Broker连接信息:<br/>");
        virtualHostMap.forEach((vhostName, vhost) ->{
            connectionInfo.append("<br/>vhost:").append(vhostName);
            vhost.getConnectionMap().forEach((connectionId, connection)->{
                connectionInfo.append("<br>&nbsp&nbsp&nbsp&nbsp connectionId:").append(connectionId);
                connection.getChannelMap().forEach((channelId, channel)->{
                    connectionInfo.append("<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp channelId:").append(channelId);
                    connectionInfo.append("<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp当前channel连接queue:");
                    if(channel.getQueueMap().isEmpty()){
                        connectionInfo.append("暂未监听任何queue");
                    }else{
                        channel.getQueueMap().forEach((queueName, queue)->{
                            connectionInfo.append("<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp queueName: " ).append(queueName);
                        });
                    }
                });
            });
        });

        return builder.toString() + connectionInfo.toString();
    }
}
