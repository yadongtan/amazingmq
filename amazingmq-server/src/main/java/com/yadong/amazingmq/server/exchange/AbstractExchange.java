package com.yadong.amazingmq.server.exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.server.vhost.VirtualHost;

public abstract class AbstractExchange implements Exchange{

    @JsonIgnore
    protected VirtualHost vhost;
    protected String exchangeName;
    protected String exchangeType;
    protected boolean duration;



    public AbstractExchange(String exchangeName, String exchangeType, boolean duration) {
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.duration = duration;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public boolean isDuration() {
        return duration;
    }

    public void setDuration(boolean duration) {
        this.duration = duration;
    }

    public VirtualHost getVhost() {
        return vhost;
    }

    public void setVhost(VirtualHost vhost) {
        this.vhost = vhost;
    }

}
