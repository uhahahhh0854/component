package com.raizumi.component.rabbitmqvu3;

import com.raizumi.component.rabbitmqvu3.entity.SimpleMLC;
import com.raizumi.component.common.utils.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Rabbitmqvu3Template extends org.springframework.amqp.rabbit.core.RabbitTemplate {

    private final Logger log = LoggerFactory.getLogger(Rabbitmqvu3Template.class);

    private ContextUtil contextUtil;

    public ContextUtil getContextUtil() {
        return contextUtil;
    }

    public void setContextUtil(ContextUtil contextUtil) {
        this.contextUtil = contextUtil;
    }

    public void setJosteinConnectionFactory(ConnectionFactory connectionFactory){
        super.setConnectionFactory(connectionFactory);
    }

    Rabbitmqvu3Template(){
        super();
    }

    Rabbitmqvu3Template(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    Rabbitmqvu3Template(ContextUtil contextUtil) {
        super();
        this.contextUtil = contextUtil;
    }

    Rabbitmqvu3Template(ContextUtil contextUtil, ConnectionFactory connectionFactory) {
        super(connectionFactory);
        this.contextUtil = contextUtil;
        super.setConnectionFactory(connectionFactory);
    }



    public String simpleSubscribe( String[] queueNames,  MessageListener messageListener){
        SimpleMLC simpleMLC = new SimpleMLC();
        simpleMLC.setQueueNames(queueNames);
        simpleMLC.setMessageListener(messageListener);

        return this.simpleSubscribe(super.getConnectionFactory(), simpleMLC);
    }

    public String simpleSubscribe( ConnectionFactory connectionFactory,  String[] queueNames,  MessageListener messageListener){
        SimpleMLC simpleMLC = new SimpleMLC();
        simpleMLC.setQueueNames(queueNames);
        simpleMLC.setMessageListener(messageListener);

        return this.simpleSubscribe(connectionFactory, simpleMLC);
    }

    public String simpleSubscribe( Queue[] queues,   MessageListener messageListener){
        SimpleMLC simpleMLC = new SimpleMLC();
        simpleMLC.setQueues(queues);
        simpleMLC.setMessageListener(messageListener);

        return this.simpleSubscribe(super.getConnectionFactory(), simpleMLC);
    }

    public String simpleSubscribe( ConnectionFactory connectionFactory,  Queue[] queues,  MessageListener messageListener){
        SimpleMLC simpleMLC = new SimpleMLC();
        simpleMLC.setQueues(queues);
        simpleMLC.setMessageListener(messageListener);

        return this.simpleSubscribe(connectionFactory, simpleMLC);
    }

    public String simpleSubscribe(SimpleMLC simpleMLC){

        return this.simpleSubscribe(super.getConnectionFactory(), simpleMLC);
    }

    /**
     * 连接前不测试连接状态，以免throw out退出
     */
    public String simpleSubscribe(ConnectionFactory connectionFactory, SimpleMLC simpleMLC) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
        Assert.notNull(simpleMLC, "SimpleMLC must not be null");

        /*try{
            connectionFactory.createConnection().close();
        }catch (Exception e){
            throw new IllegalArgumentException("Connection arguments is invalid");
        }*/


        SimpleMessageListenerContainer simpleMessageListenerContainer = simpleMLC.simpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setMissingQueuesFatal(false);
        simpleMessageListenerContainer.start();

        String key = getUUID() + "-simpleMessageListenerContainer" + subscribers.size() + System.currentTimeMillis();
        subscribers.put(key,simpleMessageListenerContainer);
        reentrantLocks.put(key,new ReentrantLock());

        return key;
    }


    private final ConcurrentHashMap<String, SimpleMessageListenerContainer> subscribers = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ReentrantLock> reentrantLocks = new ConcurrentHashMap<>();


    public boolean stop(String subName){
        boolean res = true;

        SimpleMessageListenerContainer simpleMessageListenerContainer = subscribers.get(subName);
        if (simpleMessageListenerContainer != null) {
            try{
                reentrantLocks.get(subName).lock();
                if(simpleMessageListenerContainer.isRunning()){
                    simpleMessageListenerContainer.stop();
                }
            }catch (Exception e){
                log.error("Error while stopping SimpleMessageListenerContainer. ",e);
                res = false;
            }finally {
                reentrantLocks.get(subName).unlock();
            }

        }
        return res;
    }

    public boolean start(String subName){
        boolean res = true;

        SimpleMessageListenerContainer simpleMessageListenerContainer = subscribers.get(subName);
        if (simpleMessageListenerContainer != null) {
            try{
                reentrantLocks.get(subName).lock();
                if(!simpleMessageListenerContainer.isRunning()){
                    simpleMessageListenerContainer.start();
                }
            }catch (Exception e){
                log.error("Error while starting SimpleMessageListenerContainer. ",e);
                res = false;
            }finally {
                reentrantLocks.get(subName).unlock();
            }
        }
        return res;
    }

    public boolean destroy(String subName){
        boolean res = true;

        SimpleMessageListenerContainer simpleMessageListenerContainer = subscribers.get(subName);
        if (simpleMessageListenerContainer != null) {
            try{
                reentrantLocks.get(subName).lock();
                if(simpleMessageListenerContainer.isRunning()){
                    simpleMessageListenerContainer.stop();
                    simpleMessageListenerContainer.destroy();
                }
            }catch (Exception e){
                log.error("Error while destroying SimpleMessageListenerContainer. ",e);
                res = false;
            }finally {
                reentrantLocks.get(subName).unlock();
            }
        }
        return res;
    }
}
