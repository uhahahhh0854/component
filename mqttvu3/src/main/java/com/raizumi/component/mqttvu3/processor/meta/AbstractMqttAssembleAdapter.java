package com.raizumi.component.mqttvu3.processor.meta;

import com.raizumi.component.mqttvu3.constants.ControlVar;
import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.mqttvu3.entity.Sub;
import com.raizumi.component.mqttvu3.entity.SubInfo;
import com.raizumi.component.mqttvu3.enums.Channel;
import com.raizumi.component.mqttvu3.handler.Impl.DefaultMessageAdviceCreator;
import org.aopalliance.aop.Advice;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractMqttAssembleAdapter extends AbstractMqttConnectionDisposer {

    {
        cores = Runtime.getRuntime().availableProcessors();
    }

    public final Integer cores;

    public IntegrationFlowContext flowContext;

    private final TaskScheduler adaptScheduler;

    public AbstractMqttAssembleAdapter(IntegrationFlowContext flowContext) {
        this.adaptScheduler = scheduler(cores/2);
    }

    public TaskScheduler scheduler(int threads) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(threads);
        scheduler.setThreadNamePrefix("MQTT-Thread-" + System.currentTimeMillis() + System.currentTimeMillis());
        scheduler.initialize();
        return scheduler;
    }

    public Object gateway(ApplicationContext applicationContext, MessageChannel outboundChannel, Class<?> clazz) {
        GatewayProxyFactoryBean gatewayFactory = new GatewayProxyFactoryBean(clazz);
        gatewayFactory.setDefaultRequestChannel(outboundChannel);
        gatewayFactory.setBeanFactory(applicationContext);
        gatewayFactory.setDefaultReplyTimeout(5000L);
        gatewayFactory.afterPropertiesSet();
        return gatewayFactory.getObject();
    }

    public MessageChannel createChannel(Channel type) {
        MessageChannel channel;
        switch (type) {
            case QUEUE:
                channel = new QueueChannel();
                break;
            case PRIORITY:
                channel = new PriorityChannel();
                break;
            case PUBLISH_SUBSCRIBE:
                channel = new PublishSubscribeChannel();
                break;
            default:
                channel = new DirectChannel();
                break;
        }
        return channel;
    }

    public MessageChannel createChannel(String id, Channel type, MessageHandler handler) {
        MessageChannel channel = this.createChannel(type);

        StandardIntegrationFlow errorFlow = IntegrationFlows.from(channel)
                .handle(handler)
                .get();

        flowContext.registration(errorFlow).id(id).register();

        return channel;
    }


    public void createOutFlow(MqttPahoClientFactory factory,
                              Box box,
                              String id, MessageChannel outboundChannel, MessageChannel errorChannel) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(ControlVar.PUB + box.getClientId(), factory);
        messageHandler.setAsync(Boolean.TRUE);
        messageHandler.setDefaultQos(box.getQos());
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        messageHandler.setConverter(converter);

        /*  暂不处理to many publishes的问题，同步发送解决qos=2的问题
         */
        messageHandler.setAsync(false);

        /*   集成流会依据是否存在而选择引用或者新建
         *   在发送的时候mqttPahoMessageHandler默认讲payload转换成String，需要特殊配置：1.显式设定使用原始payload；2.使用特定Transform方法确定payload不会被转换
         *   这里使用的GenericTransformer
         * */


        DefaultMessageAdviceCreator creator = (DefaultMessageAdviceCreator)box.getMessageAdviceCreator();
        creator.setErrorChannel(errorChannel);
        Advice advice = creator.getAdvice();
        Consumer<GenericEndpointSpec<MessageTransformingHandler>> endpointConfigurer = e -> {
            e.advice(advice);
        };
        StandardIntegrationFlow flow = IntegrationFlows.from(outboundChannel)
                .transform(box.getTransformingDescriptor().outTransform(box.getMode()),endpointConfigurer)
                .handle(messageHandler)
                .get();

        flowContext.registration(flow).id(id).register();
    }

    public void createInFlow(MqttPahoClientFactory factory, String id, Sub sub,
                             MessageChannel inboundChannel, MessageChannel errorChannel) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(sub.getClientId(), factory);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setErrorChannel(errorChannel);


        adapter.setQos(sub.getQos());
        adapter.addTopic(sub.getTopics());
        if (subscribers.size() > 3){
            adapter.setTaskScheduler(scheduler(cores/2));
        }else{
            adapter.setTaskScheduler(this.adaptScheduler);
        }

        /*  复用advice，出现错误转发到对应errorChannel，根据错误类型处理*/
        DefaultMessageAdviceCreator creator = (DefaultMessageAdviceCreator) sub.getMessageAdviceCreator();
        creator.setErrorChannel(errorChannel);
        Advice advice = creator.getAdvice();
        Consumer<GenericEndpointSpec<MessageTransformingHandler>> endpointConfigurer = e -> {
            e.advice(advice);
        };

        /*  .handler()无法直接配置endpointConfigurer，直接配置成自带advice的handler*/
        ServiceActivatingHandler handler = new ServiceActivatingHandler(sub.getMessageHandler());
        handler.setAdviceChain(
                Collections.singletonList(advice)
        );

        GenericTransformer<byte[], Object> transformer = sub.getTransformingDescriptor().inTransform(sub.getMode());

        /*  解密*/
        /*  业务处理*/
        StandardIntegrationFlow flow = sub.getBranch() ? IntegrationFlows.from(adapter)
                /*  解密*/
                .transform(transformer, endpointConfigurer)
                /*  业务处理*/
                .channel(inboundChannel)
                .handle(handler)
                .get() :

                IntegrationFlows.from(adapter)
                        /*  解密*/
                .transform(transformer, endpointConfigurer)
                        /*  业务处理*/
                .aggregate(
                            a -> a
                                 .correlationStrategy(m -> m.getHeaders().get(MqttHeaders.RECEIVED_TOPIC))
                                 .taskScheduler(this.adaptScheduler)
                                 .releaseStrategy(new MessageCountReleaseStrategy(sub.getBatchSize()))
                                 .groupTimeout(5000)
                                 .sendPartialResultOnExpiry(true)
                                 .expireGroupsUponCompletion(true)
                )
                .channel(inboundChannel)
                .handle(handler)
                .get()
                ;

        flowContext.registration(flow)
                .id(id)
                .register();

        SubInfo subInfo = SubInfo.builder()
                .flowId(id)
                .clientId(sub.getClientId())
                .support(adapter)
                .topics(Arrays.asList(sub.getTopics()))
                .active(sub.getActivate())
                .build();
        subscribers.put(id, subInfo);

        if (sub.getActivate()){
            adapter.start();
        }
    }

    public final ConcurrentHashMap<String, SubInfo> subscribers = new ConcurrentHashMap<>();

    public void addTopics(String flowId, String... topics) {
        MessageProducerSupport support = subscribers.get(flowId).getSupport();
        MqttPahoMessageDrivenChannelAdapter adapt = (MqttPahoMessageDrivenChannelAdapter) support;
        adapt.addTopic(topics);
    }

    public void addTopics(String flowId, Integer qos, String... topics) {
        MessageProducerSupport support = subscribers.get(flowId).getSupport();
        MqttPahoMessageDrivenChannelAdapter adapt = (MqttPahoMessageDrivenChannelAdapter) support;
        adapt.addTopic(topics);
        adapt.setQos(qos);
    }

    public void removeTopics(String flowId, String... topics) {
        MessageProducerSupport support = subscribers.get(flowId).getSupport();
        MqttPahoMessageDrivenChannelAdapter adapt = (MqttPahoMessageDrivenChannelAdapter) support;
        adapt.removeTopic(topics);
    }

    public void stopConsume(String flowId){
        if (!subscribers.containsKey(flowId)){
            return;
        }
        subscribers.get(flowId).getSupport().stop();
    }

    public void unsubscribe(String flowId){
        if (!subscribers.containsKey(flowId)){
            return;
        }
        flowContext.remove(flowId);
        subscribers.get(flowId).getSupport().stop();
        subscribers.remove(flowId);
    }

    public List<SubInfo> listening(String flowId){
        if (flowId == null){
            return new ArrayList<>(subscribers.values());
        }

        if (!subscribers.containsKey(flowId)){
            return null;
        }
        return Collections.singletonList(subscribers.get(flowId));
    }
}
