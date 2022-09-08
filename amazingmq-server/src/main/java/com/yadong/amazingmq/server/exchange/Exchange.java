package com.yadong.amazingmq.server.exchange;


import com.yadong.amazingmq.server.vhost.VirtualHost;

/**
* @author YadongTan
* @date 2022/9/4 23:15
* @Description the super interface of exchanges
*/
public interface Exchange {

    public VirtualHost getVhost();

    public void setVhost(VirtualHost vhost);

    public String getExchangeName();

    public void setExchangeName(String exchangeName);

    public String getExchangeType();

    public void setExchangeType(String exchangeType);

    public boolean isDuration();

    public void setDuration(boolean duration);

}
