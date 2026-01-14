Custom Components Usage

This project provides two custom components for messaging:

RabbitMQ Custom Component (Publisher + SimpleMLC)

MQTT Custom Component (Box + Sub)

These components are designed to simplify the configuration and usage of RabbitMQ and MQTT in your Java/Spring project.

1. RabbitMQ Custom Component
1.1. Configuration Example

```
// Create CachingConnectionFactory
CachingConnectionFactory factory = new CachingConnectionFactory();
factory.setHost("localhost");
factory.setPort(5672);  // Default RabbitMQ port
factory.setUsername("root");
factory.setPassword("Dh@246810");
factory.setVirtualHost("/"); // Virtual host
factory.setChannelCacheSize(25); // Cache 25 channels

// Create Publisher
Publisher publisher = new Publisher(factory);

// Build RabbitTemplate using a custom admin utility
RabbitTemplate rabbitTemplate = rabbitmqAdminister.buildTemplate(publisher);

// Register RabbitTemplate to context for global usage
contextUtil.register("rabbitmqTemplate", RabbitTemplate.class, rabbitTemplate);

// Setup a simple message listener container
SimpleMLC simpleMLC = new SimpleMLC();
simpleMLC.setQueueNames(new String[]{"TEST"});

// Subscribe to the queue
rabbitTemplate.simpleSubscribe(simpleMLC);
```


1.2. Key Features

Connection caching via CachingConnectionFactory

Automatic listener registration using SimpleMLC

Global template access through ContextUtil

1.3. RabbitMQ Data Flow

```
[Publisher] --send--> [RabbitTemplate] --AMQP--> [RabbitMQ Broker] --deliver--> [SimpleMLC Listener] --process--> [Service Logic]


Publisher → sends messages via RabbitTemplate

SimpleMLC → listens to queue "TEST" and processes messages
```


2. MQTT Custom Component
2.1. Configuration Example

```
Box box = new Box();

// Encryption settings
box.setSecretKey("2B7E151628AED2A6ABF7158809CF4F3C");
box.setAlgorithm("OFB");

// MQTT connection settings
box.setHost("tcp://localhost:1883");
box.setQos(2);
box.setUsername("root");
box.setPassword("Dh@246810");
box.setClientId(UUID.randomUUID().toString());

// Subscriber configuration
Sub sub = new Sub(new MessageHandler() {
    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        System.out.println(message.getPayload().toString());
    }
});
sub.setActivate(Boolean.TRUE);
sub.setQos(2);
sub.setTopics(new String[]{"trap.data"});

// Encryption for subscriber
sub.setSecretKey("2B7E151628AED2A6ABF7158809CF4F3C");
sub.setAlgorithm("OFB");

// Attach subscriber to Box
box.setSub(sub);

// Register MQTT template globally
MqttTemplate mqttTemplate = mqttAdminister.register(box);
contextUtil.register("mqttTemplate", MqttTemplate.class, mqttTemplate);
```


2.2. Key Features

Secure messaging with configurable encryption (SecretKey + Algorithm)

Flexible subscription via Sub object

Dynamic MQTT client IDs using UUID

Global template access through ContextUtil

2.3. MQTT Data Flow

```
[Publisher or External MQTT Client] --publish--> [MQTT Broker] --deliver--> [Box/Sub Subscriber] --process--> [Service Logic]


Box → configures MQTT connection and security

Sub → subscribes to topics like "trap.data"

```

3. Notes

Ensure RabbitMQ and MQTT servers are running and accessible with the provided credentials.

Customize queue names, topics, and encryption settings according to your project requirements.

ContextUtil.register() allows you to reuse the messaging templates across different services or components in your application.

4. Example Usage in Service

```
// Using RabbitMQ
RabbitTemplate rabbitTemplate = contextUtil.get("rabbitmqTemplate", RabbitTemplate.class);
rabbitTemplate.convertAndSend("TEST", "Hello RabbitMQ!");

// Using MQTT
MqttTemplate mqttTemplate = contextUtil.get("mqttTemplate", MqttTemplate.class);
mqttTemplate.publish("trap.data", "Hello MQTT!".getBytes(), 2, false);
```
