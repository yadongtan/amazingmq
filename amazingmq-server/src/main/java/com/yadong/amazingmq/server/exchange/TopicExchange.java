package com.yadong.amazingmq.server.exchange;

import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.queue.OutOfMaxLengthException;

import java.util.Map;

/**
* @author YadongTan
* @date 2022/9/10 11:01
* @Description 匹配
*/
public class TopicExchange extends AbstractExchange{

    public TopicExchange(String exchangeName, String exchangeType, boolean duration) {
        super(exchangeName, exchangeType, duration);
    }


    @Override
    public boolean sendMessageToQueue(String routingKey, Message message) throws OutOfMaxLengthException {
        for (Map.Entry<String, AmazingMqQueue> entry : queueMap.entrySet()) {
            if(topicMatches(entry.getKey().split("\\."), routingKey.split("\\."), 0, 0)){
                entry.getValue().offer(message);
            }
        }
        return true;
    }

    private static boolean topicMatches(String[] topic, String[] routingKey, int tIndex, int rIndex){
        if(topic.length == tIndex && routingKey.length == rIndex){
            return true;
        }else if(topic.length == tIndex || routingKey.length == rIndex){
            return false;
        }
        if((topic[tIndex].equals("*") && (!routingKey[rIndex].equals("#")) )
        || ((!topic[tIndex].equals("#") )&& routingKey[rIndex].equals("*"))
        ){
            return topicMatches(topic, routingKey, ++tIndex, ++rIndex);
        }else if(topic[tIndex].equals("#") || routingKey[rIndex].equals("#")){
            return true;
        }else{
            return topic[tIndex].equals(routingKey[rIndex]) && topicMatches(topic, routingKey, ++tIndex, ++rIndex);
        }
    }
//
//    public static void main(String[] args) {
//        String topic = "zsv.#";
//        String routingKey = "zsv.ama.xxx";
//        System.out.println(topicMatches(topic.split("\\."),routingKey.split("\\."),0,0));
//
//        String topic2 = "zsv.*.aad";
//        String routingKey2 = "zsv.ama.aad";
//        System.out.println(topicMatches(topic2.split("\\."),routingKey2.split("\\."),0,0));
//    }


}
