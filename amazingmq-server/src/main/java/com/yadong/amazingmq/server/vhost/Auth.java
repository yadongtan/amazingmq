package com.yadong.amazingmq.server.vhost;


import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.property.UserProperties;

/**
* @author YadongTan
* @date 2022/9/6 10:49
* @Description 用来判断一个用户是否具有某个vhost的访问权限
*/
public class Auth {

    public static boolean accessible(UserProperties userProperties){
        UserProperties localUserProperties = AmazingMqBroker.getInstance().getUserPropertiesMap().get(userProperties.getUsername());
        if (localUserProperties == null) {
            //没有这个用户
            return false;
        }
        if (localUserProperties.getPassword().equals(userProperties.getPassword())) {
            //检测有无访问权限
            if(localUserProperties.getAllVhost().containsAll(userProperties.getAllVhost())){
                //有访问权限
                return true;
            }else{
                //无访问权限
                return false;
            }
        }else{
            //密码错误
            return false;
        }
    }
}
