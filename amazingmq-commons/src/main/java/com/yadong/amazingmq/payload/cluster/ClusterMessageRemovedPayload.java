package com.yadong.amazingmq.payload.cluster;

public class ClusterMessageRemovedPayload {

    private String vhost;   //vhost的path
    private String queueName;   //队列名称
    private int messageId;   //消息id

    public ClusterMessageRemovedPayload() {
    }

    public ClusterMessageRemovedPayload(String vhost, String queueName, int messageId) {
        this.vhost = vhost;
        this.queueName = queueName;
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
}
