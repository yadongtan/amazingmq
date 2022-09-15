package com.yadong.amazingmq.payload.cluster;

import com.yadong.amazingmq.payload.BindingPayload;
import com.yadong.amazingmq.payload.ConnectionCreatedPayload;
import com.yadong.amazingmq.payload.Payload;

public class ClusterComponentCreatedPayload implements Payload{

    private String createComponentPayloadJson;
    private String virtualHost;

    public ClusterComponentCreatedPayload(){}

    public ClusterComponentCreatedPayload(String payload, String virtualHost){
        this.createComponentPayloadJson = payload;
        this.virtualHost = virtualHost;
    }

    public String getCreateComponentPayloadJson() {
        return createComponentPayloadJson;
    }

    public void setCreateComponentPayloadJson(String createComponentPayloadJson) {
        this.createComponentPayloadJson = createComponentPayloadJson;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }
}
