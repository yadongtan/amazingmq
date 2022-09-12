package com.yadong.amazingmq.server.queue;


import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YadongTan
 * @date 2022/9/9 12:35
 * @Description 事件调度者
 */
@Deprecated
public class QueueScheduler {

    private static final QueueScheduler _INSTANCE = new QueueScheduler();
    private final List<AmazingMqQueue> queueList = new CopyOnWriteArrayList<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(8);

    private QueueScheduler(){}

    public static QueueScheduler getInstance(){
        return _INSTANCE;
    }

    public void addQueue(AmazingMqQueue queue){
        queueList.add(queue);
    }

    public void removeQueue(AmazingMqQueue queue){
        queueList.remove(queue);
    }

    public void startScheduler(){
        new Thread( () -> {
            while (true) {
                for (AmazingMqQueue queue : queueList) {
                    System.out.println("...." + queue.hashMessage());
                    if (queue.hashMessage()) {
                        threadPool.submit((Runnable) queue::trySendMessageToConsumer);
                    }
                }
            }
        }).start();
    }
}
