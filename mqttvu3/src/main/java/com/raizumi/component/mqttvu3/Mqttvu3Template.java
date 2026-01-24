package com.raizumi.component.mqttvu3;


import com.raizumi.component.mqttvu3.constants.ControlVar;
import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.mqttvu3.entity.Disposer;
import com.raizumi.component.mqttvu3.entity.Sub;
import com.raizumi.component.mqttvu3.enums.Channel;
import com.raizumi.component.common.handler.DEHandleDescriptor;
import com.raizumi.component.mqttvu3.handler.Impl.DefaultErrorMessageHandler;
import com.raizumi.component.common.handler.SEHandleDescriptor;
import com.raizumi.component.mqttvu3.handler.TransformingDescriptor;
import com.raizumi.component.mqttvu3.processor.Sender;
import com.raizumi.component.mqttvu3.processor.ToSubscribe;
import com.raizumi.component.mqttvu3.processor.meta.AbstractMqttAssembleAdapter;
import com.raizumi.component.common.utils.ContextUtil;
import com.raizumi.component.common.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.raizumi.component.mqttvu3.constants.ControlVar.*;

public class Mqttvu3Template extends AbstractMqttAssembleAdapter implements ToSubscribe {
    private static final Logger log = LoggerFactory.getLogger(Mqttvu3Template.class);


    public Box box;

    private Sender sender;

    public ContextUtil contextUtil;

    private volatile Boolean isCompleted = false;

    public Mqttvu3Template(Box box, ContextUtil contextUtil) {
        super(contextUtil.getBean(IntegrationFlowContext.class));

        this.box = box;
        this.contextUtil = contextUtil;

        this.MQTT_SENDER = this.MQTT_SENDER + box.getClientId();
        this.MQTT_OUTBOUND_FLOW = this.MQTT_OUTBOUND_FLOW + box.getClientId();
        this.MQTT_INBOUND_FLOW = this.MQTT_INBOUND_FLOW + box.getClientId();
        this.MQTT_ERROR_FLOW = this.MQTT_ERROR_FLOW + box.getClientId();
        this.MQTT_OUTBOUND_CHANNEL = this.MQTT_OUTBOUND_CHANNEL + box.getClientId();
        this.MQTT_INBOUND_CHANNEL = this.MQTT_INBOUND_CHANNEL + box.getClientId();
        this.MQTT_ERROR_CHANNEL = this.MQTT_ERROR_CHANNEL + box.getClientId();

        this.box.process();
        this.register(box);
    }

    private void beforeSend(String[] topics, int[] qos) {
        if (!isCompleted) {
            throw new RuntimeException("Sender is not ready");
        }
        if (topics == null) {
            throw new NullPointerException("Topics is null");
        }
        if (topics.length != qos.length) {
            throw new IllegalArgumentException("Topics length is not equal to qos length");
        }

    }

    public void send(Object payload) {
        String[] topics = (box.getSub() != null && box.getSub().getTopics() != null && box.getSub().getTopics().length != 0) ?
                box.getSub().getTopics() : new String[]{"default"};
        int[] qos = new int[topics.length];
        Arrays.fill(qos, box.getQos());
        this.beforeSend(topics, qos);
        sender.send(topics, qos, payload);
    }

    public void send(String topic, Object payload) {

        String[] topics = new String[]{topic};
        int[] qos = new int[]{box.getQos()};
        beforeSend(topics, qos);
        sender.send(topics, payload);
    }

    public void send(String topic, int qos, Object payload) {
        String[] topics = new String[]{topic};
        int[] qos_s = new int[]{qos};
        beforeSend(topics, qos_s);
        sender.send(topics, qos, payload);
    }

    public void send(String[] topics, Object payload) {
        int[] qos = new int[topics.length];
        Arrays.fill(qos, box.getQos());
        this.beforeSend(topics, qos);
        sender.send(topics, payload);
    }

    public void send(String[] topics, int qos, Object payload) {
        int[] qos_s = new int[topics.length];
        Arrays.fill(qos_s, box.getQos());
        beforeSend(topics, qos_s);
        sender.send(topics, qos, payload);
    }

    public void send(String[] topics, int[] qos, Object payload) {
        beforeSend(topics, qos);
        sender.send(topics, qos, payload);
    }


    private String MQTT_SENDER = "MqttSender-";

    private String MQTT_OUTBOUND_FLOW = "MqttOutboundFlow-";

    private String MQTT_INBOUND_FLOW = "MqttInboundFlow-";

    private String MQTT_ERROR_FLOW = "MqttErrorFlow-";

    private String MQTT_OUTBOUND_CHANNEL = "MqttOutboundChannel-";

    private String MQTT_INBOUND_CHANNEL = "MqttDefaultInboundChannel-";

    private String MQTT_ERROR_CHANNEL = "MqttErrorChannel-";


    private void unregister() {
        isCompleted = false;
        try {
            if (flowContext == null) {
                flowContext = contextUtil.getBean(IntegrationFlowContext.class);
                flowContext.remove(MQTT_ERROR_FLOW);
            }

            if (!contextUtil.unregister(MQTT_SENDER)
                    || !contextUtil.unregister(MQTT_OUTBOUND_CHANNEL)
                    || !contextUtil.unregister(MQTT_ERROR_CHANNEL)){
                throw new RuntimeException();
            }
        } catch (Exception e) {
            log.error("[{}]: unregistration failure{}", ControlVar.MQTT + box.getClientId(), e.toString());
        }
    }

    private PublishSubscribeChannel errorChannel;

    private void register(Box box) {
        try {
            if (this.flowContext == null) {
                this.flowContext = contextUtil.getBean(IntegrationFlowContext.class);
            }

            errorChannel = (PublishSubscribeChannel) super.createChannel(MQTT_ERROR_FLOW, Channel.PUBLISH_SUBSCRIBE, new DefaultErrorMessageHandler());

            DirectChannel outboundChannel = (DirectChannel) super.createChannel(Channel.DIRECT);

            if (!contextUtil.register(MQTT_OUTBOUND_CHANNEL, DirectChannel.class, outboundChannel)) {
                throw new RuntimeException("[MqttTemplate]: Some exception happened when registering out channel]");
            }

            if (!contextUtil.register(MQTT_ERROR_CHANNEL, PublishSubscribeChannel.class, errorChannel)) {
                throw new RuntimeException("[MqttTemplate]: Some exception happened when registering error channel]");
            }

            super.createOutFlow(mqttClientFactory(box), box,
                    MQTT_OUTBOUND_FLOW, outboundChannel, errorChannel);

            /*  默认主题*/
            if (box.getSub() != null) {
                Sub sub = box.getSub();
                sub.setUsername(box.getUsername());
                sub.setPassword(box.getPassword());
                sub.setSsl(box.getSsl());
                sub.process();
                List<String> subscribe = this.subscribe(sub);
            }

            sender = (Sender) super.gateway(contextUtil.getApplicationContext(), outboundChannel, Sender.class);

        } catch (Exception e) {
            log.error("[{}]: registration failure{}", ControlVar.MQTT + box.getClientId(), e.toString());
            return;
        }

        isCompleted = true;
    }

    private MessageChannel createChannel(Sub sub, long timestamp) {
        if (sub.getErrorMessageHandler() != null &&
                ReflectionUtil.isOverride(sub.getErrorMessageHandler().getClass(), CRYPT_ERROR_HANDLER, SERIALIZE_ERROR_HANDLER, TRANSACTION_ERROR_HANDLER)) {

            MessageChannel errorChannel = super.createChannel(MQTT_ERROR_FLOW + timestamp, Channel.DIRECT, sub.getErrorMessageHandler());
            contextUtil.register(MQTT_ERROR_CHANNEL + timestamp, MessageChannel.class, errorChannel);
            return errorChannel;
        } else {
            return this.errorChannel;
        }
    }

    @Override
    public List<String> subscribe(Sub sub) {
        List<String> result = new ArrayList<>();

        String shared_prefix = String.format("$share/%s/", sub.getGroupId());

        if (sub.isShared()) {
            List<String> topics = new ArrayList<>();
            for (String topic : sub.getTopics()) {
                topics.add(shared_prefix + topic);
            }
            sub.setTopics(topics.toArray(new String[0]));
        } else {
            sub.setConcurrency(1);
        }
        int concurrency = sub.getConcurrency();

        while (concurrency-- > 0) {
            String s = this.subscribeImpl(sub);
            result.add(s);
        }
        return result;
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        return this.subscribe(sub);
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  MessageHandler errorMessageHandler,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        sub.setMessageHandler(messageHandler);
        sub.setErrorMessageHandler(errorMessageHandler);

        return this.subscribe(sub);
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  Disposer disposer,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        sub.setMessageHandler(messageHandler);
        sub.setDisposer(disposer);

        return this.subscribe(sub);
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  MessageHandler errorMessageHandler,
                                  Disposer disposer,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        sub.setMessageHandler(messageHandler);
        sub.setErrorMessageHandler(errorMessageHandler);
        sub.setDisposer(disposer);

        return this.subscribe(sub);
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  TransformingDescriptor transformingDescriptor,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        sub.setTransformingDescriptor(transformingDescriptor);
        return this.subscribe(sub);
    }

    @Override
    public List<String> subscribe(MessageHandler messageHandler,
                                  DEHandleDescriptor deHandleDescriptor,
                                  SEHandleDescriptor seHandleDescriptor,
                                  String... topics) {
        Sub sub = new Sub(messageHandler);
        sub.setHost(box.getHost());
        sub.setUsername(box.getUsername());
        sub.setPassword(box.getPassword());
        sub.setTopics(topics);
        sub.setDeHandleDescriptor(deHandleDescriptor);
        sub.setSeHandleDescriptor(seHandleDescriptor);
        return this.subscribe(sub);
    }

    private String subscribeImpl(Sub sub) {
        try {
            synchronized (this) {
                //  初始化所有处理器
                String valid = sub.valid();

                if (valid != null) {
                    throw new RuntimeException("[MqttTemplate]: Subscription information is illegal, " + valid);
                }

                sub.process();

                long timestamp = System.currentTimeMillis();

                sub.setClientId(box.getClientId() + timestamp);

                if (sub.getTopics() == null || sub.getTopics().length == 0) {
                    return null;
                }

                if (sub.getMessageHandler() == null) {
                    return null;
                }


                String id = MQTT_INBOUND_FLOW + timestamp;

                DirectChannel inBoundChannel = new DirectChannel();
                contextUtil.register(MQTT_INBOUND_CHANNEL + timestamp, DirectChannel.class, inBoundChannel);

                super.createInFlow(mqttClientFactory(
                        sub.getUsername(), sub.getPassword(), sub.getConnectionTimeOut(), sub.getCleanSession(), sub.getKeepAlive(), sub.getAutomaticReconnect(), sub.getSsl(), sub.getHost()
                ), id, sub, inBoundChannel, this.createChannel(sub, timestamp));

                return id;
            }
        } catch (Exception e) {
            throw new RuntimeException(ControlVar.MQTT + box.getClientId() + " subscribe failure " + e.toString());
        }
    }
}
