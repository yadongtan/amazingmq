package com.yadong.amazingmq.server.property;


import java.util.ArrayList;
import java.util.List;

/**
* @author YadongTan
* @date 2022/9/6 10:24
* @Description 一个用户的信息,包括他可访问的vhost
*/
public class UserProperties {

    //allow vhost
    private List<String> allVhost = new ArrayList<>();

    private String username;
    private String password;

    public List<String> addVhost(String vhost){
        allVhost.add(vhost);
        return allVhost;
    }
    public List<String> removeVhost(String vhost){
        allVhost.remove(vhost);
        return allVhost;
    }

    public UserProperties(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserProperties(String username, String password, String vhost) {
        this.username = username;
        this.password = password;
        this.allVhost.add(vhost);
    }


    public List<String> getAllVhost() {
        return allVhost;
    }

    public void setAllVhost(List<String> allVhost) {
        this.allVhost = allVhost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
