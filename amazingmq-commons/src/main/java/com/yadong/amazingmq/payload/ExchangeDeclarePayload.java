package com.yadong.amazingmq.payload;


public class ExchangeDeclarePayload implements Payload{


    private String exchangeName;    //交换机名称
    private String exchangeType;    //类型
    private boolean duration;   //是否持久化

    public String getExchangeName() {
        return exchangeName;
    }

    public ExchangeDeclarePayload setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public ExchangeDeclarePayload setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
        return this;
    }

    public boolean isDuration() {
        return duration;
    }

    public ExchangeDeclarePayload setDuration(boolean duration) {
        this.duration = duration;
        return this;
    }
}
