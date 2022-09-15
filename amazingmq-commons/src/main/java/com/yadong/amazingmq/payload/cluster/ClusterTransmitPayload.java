package com.yadong.amazingmq.payload.cluster;

import com.yadong.amazingmq.payload.Payload;

public class ClusterTransmitPayload implements Payload{

    private String originalPayloadJson;
    private String virtualHost;

    public ClusterTransmitPayload(){}

    public ClusterTransmitPayload(String payload, String virtualHost){
        this.originalPayloadJson = payload;
        this.virtualHost = virtualHost;
    }

    public String getOriginalPayloadJson() {
        return originalPayloadJson;
    }

    public void setOriginalPayloadJson(String originalPayloadJson) {
        this.originalPayloadJson = originalPayloadJson;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }
}
