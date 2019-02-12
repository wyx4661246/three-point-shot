package com.malzahar.tps.broker;

import com.malzahar.tps.common.Configuration;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import com.malzahar.tps.store.MessageStoreConfig;

import java.util.Properties;

public class BrokerController {

    private BrokerConfig brokerConfig;
    private NettyServerConfig nettyServerConfig;
    private NettyClientConfig nettyClientConfig;
    private MessageStoreConfig messageStoreConfig;

    private Configuration configuration;


    public BrokerController(BrokerConfig brokerConfig, NettyServerConfig nettyServerConfig, NettyClientConfig nettyClientConfig, MessageStoreConfig messageStoreConfig) {
        this.brokerConfig = brokerConfig;
        this.nettyServerConfig = nettyServerConfig;
        this.nettyClientConfig = nettyClientConfig;
        this.messageStoreConfig = messageStoreConfig;

        configuration = new Configuration();
    }

    public boolean initialize(){
        return Boolean.TRUE;
    }

    public void start() throws Exception {

    }

    public void registerConfig(Properties extProperties){
        configuration.registerConfig(extProperties);
    }

    public void shutdown() {

    }
}
