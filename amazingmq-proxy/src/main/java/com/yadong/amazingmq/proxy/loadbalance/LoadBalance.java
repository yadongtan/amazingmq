package com.yadong.amazingmq.proxy.loadbalance;




import com.yadong.amazingmq.property.HostInfo;

import java.util.List;

/**
 * @author YadongTan
 * @date 2022/8/31 19:33
 * @Description 负载均衡策略
 */
public interface LoadBalance {

    HostInfo doSelect(List<HostInfo> hostInfos);

}
