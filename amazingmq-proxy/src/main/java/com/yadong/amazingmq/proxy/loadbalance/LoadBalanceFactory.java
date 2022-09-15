package com.yadong.amazingmq.proxy.loadbalance;


public class LoadBalanceFactory {

    public static final String HASH = "hash";
    public static final String LEAST_ACTIVE = "leastActive";
    public static final String RANDOM = "random";
    public static final String ROUND = "round";
    public static final String SHORTEST_RESPONSE = "shortestResponse";


    public static LoadBalance getLoadBalance(String LoadBalanceStrategy) throws UnknownLoadBalanceException {
        switch (LoadBalanceStrategy) {
            case "random":
                return new RandomLoadBalance();
            default:
                throw new UnknownLoadBalanceException("未知的LoadBalance:[" + LoadBalanceStrategy + "]");
        }
    }
}
