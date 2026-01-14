package com.raizumi.component.rabbitmqvu3.entity;

import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;

import java.util.concurrent.ExecutorService;

public class Connection {

    private String host = "localhost";
    private Integer port = 5672;
    private String username = "admin";
    private String password = "admin";
    private String virtualHost = "/";
    private String uri;
    private int connectionTimeout = 0;
    private Integer requestedHeartbeat = 60;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public void setRequestedHeartbeat(Integer requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    public AbstractConnectionFactory.AddressShuffleMode getAddressShuffleMode() {
        return addressShuffleMode;
    }

    public void setAddressShuffleMode(AbstractConnectionFactory.AddressShuffleMode addressShuffleMode) {
        this.addressShuffleMode = addressShuffleMode;
    }

    public CachingConnectionFactory.ConfirmType getPublisherConfirmType() {
        return publisherConfirmType;
    }

    public void setPublisherConfirmType(CachingConnectionFactory.ConfirmType publisherConfirmType) {
        this.publisherConfirmType = publisherConfirmType;
    }

    public boolean isPublisherReturns() {
        return publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }

    public ConnectionNameStrategy getConnectionNameStrategy() {
        return connectionNameStrategy;
    }

    public void setConnectionNameStrategy(ConnectionNameStrategy connectionNameStrategy) {
        this.connectionNameStrategy = connectionNameStrategy;
    }

    public Integer getCloseTimeout() {
        return closeTimeout;
    }

    public void setCloseTimeout(Integer closeTimeout) {
        this.closeTimeout = closeTimeout;
    }

    public CachingConnectionFactory.CacheMode getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(CachingConnectionFactory.CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public int getConnectionCacheSize() {
        return connectionCacheSize;
    }

    public void setConnectionCacheSize(int connectionCacheSize) {
        this.connectionCacheSize = connectionCacheSize;
    }

    public int getChannelCacheSize() {
        return channelCacheSize;
    }

    public void setChannelCacheSize(int channelCacheSize) {
        this.channelCacheSize = channelCacheSize;
    }

    public long getChannelCheckoutTimeout() {
        return channelCheckoutTimeout;
    }

    public void setChannelCheckoutTimeout(long channelCheckoutTimeout) {
        this.channelCheckoutTimeout = channelCheckoutTimeout;
    }

    public boolean isTopologyRecovery() {
        return topologyRecovery;
    }

    public void setTopologyRecovery(boolean topologyRecovery) {
        this.topologyRecovery = topologyRecovery;
    }

    private ExecutorService executor;
    private String[] addresses;
    private AbstractConnectionFactory.AddressShuffleMode addressShuffleMode = AbstractConnectionFactory.AddressShuffleMode.RANDOM;
    private CachingConnectionFactory.ConfirmType publisherConfirmType = CachingConnectionFactory.ConfirmType.NONE;
    private boolean publisherReturns = false;
    private ConnectionNameStrategy connectionNameStrategy;
    private Integer closeTimeout = 30000;
    private CachingConnectionFactory.CacheMode cacheMode = CachingConnectionFactory.CacheMode.CHANNEL;
    private int connectionCacheSize = 1;
    private int channelCacheSize = 25;
    private long channelCheckoutTimeout = 0;
    private boolean topologyRecovery = true;

    public CachingConnectionFactory cacheConnection() {
        CachingConnectionFactory factory;

        if (uri != null && !uri.isEmpty()) {
            factory = new CachingConnectionFactory(uri);
        } else if (addresses != null && addresses.length > 0) {
            factory = new CachingConnectionFactory();
            factory.setAddresses(String.join(",", addresses));
        } else {
            factory = new CachingConnectionFactory(host, port);
        }

        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);

        factory.setConnectionTimeout(connectionTimeout);
        factory.setRequestedHeartBeat(requestedHeartbeat);
        factory.setCloseTimeout(closeTimeout);
        if (connectionNameStrategy != null) factory.setConnectionNameStrategy(connectionNameStrategy);
        if (executor != null) factory.setExecutor(executor);

        factory.setCacheMode(cacheMode);
        factory.setConnectionCacheSize(connectionCacheSize);
        factory.setChannelCacheSize(channelCacheSize);
        factory.setChannelCheckoutTimeout(channelCheckoutTimeout);

        factory.setPublisherConfirmType(publisherConfirmType);
        factory.setPublisherReturns(publisherReturns);

        factory.getRabbitConnectionFactory().setTopologyRecoveryEnabled(topologyRecovery);

        return factory;
    }
}
