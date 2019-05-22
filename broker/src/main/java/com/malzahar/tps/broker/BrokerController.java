package com.malzahar.tps.broker;

import com.alibaba.fastjson.JSON;
import com.malzahar.tps.common.Configuration;
import com.malzahar.tps.common.RemotingUtil;
import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.remoting.RemotingServer;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingClient;
import com.malzahar.tps.remoting.netty.NettyRemotingServer;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.header.namesrv.RegisterBrokerRequestHeader;
import com.malzahar.tps.store.MessageStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BrokerController {

    private final static Logger logger = LoggerFactory.getLogger(BrokerController.class);


    private BrokerConfig brokerConfig;
    private NettyServerConfig nettyServerConfig;
    private NettyClientConfig nettyClientConfig;
    private MessageStoreConfig messageStoreConfig;

    private Configuration configuration;

    private RemotingServer remotingServer;
    private NettyRemotingClient remotingClient;

    public BrokerController(BrokerConfig brokerConfig, NettyServerConfig nettyServerConfig, NettyClientConfig nettyClientConfig, MessageStoreConfig messageStoreConfig) {
        this.brokerConfig = brokerConfig;
        this.nettyServerConfig = nettyServerConfig;
        this.nettyClientConfig = nettyClientConfig;
        this.messageStoreConfig = messageStoreConfig;

        configuration = new Configuration();
    }

    public boolean initialize(){
        remotingServer = new NettyRemotingServer(this.nettyServerConfig);
        remotingClient = new NettyRemotingClient(this.nettyClientConfig);
        return Boolean.TRUE;
    }

    public void start() throws Exception {
        remotingServer.start();
        remotingClient.start();
        //namesrv注册
        RegisterBrokerRequestHeader registerBrokerRequestHeader = new RegisterBrokerRequestHeader();
        registerBrokerRequestHeader.setBrokerId(0l);
        registerBrokerRequestHeader.setBrokerName("broker-a");
        registerBrokerRequestHeader.setBrokerAddr(getBrokerAddr());
        registerBrokerRequestHeader.setHaServerAddr(getBrokerAddr());
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.ADD_BROKER, registerBrokerRequestHeader);
        RemotingCommand response = remotingClient.invokeSync(configuration.getConfig("namesrv.addr"), request, 1000l);
        logger.info("==== test {}", JSON.toJSONString(response));
    }

    public void registerConfig(Properties extProperties){
        configuration.registerConfig(extProperties);
    }

    public void shutdown() {

    }

    public String getBrokerAddr() {
        return this.brokerConfig.getBrokerIP1() + ":" + this.nettyServerConfig.getListenPort();
    }
}
