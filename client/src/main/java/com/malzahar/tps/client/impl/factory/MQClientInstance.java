package com.malzahar.tps.client.impl.factory;

import com.malzahar.tps.client.config.ClientConfig;
import com.malzahar.tps.client.impl.MQClientAPIImpl;
import com.malzahar.tps.client.processor.ClientRemotingProcessor;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingClient;

public class MQClientInstance {

    private final NettyClientConfig nettyClientConfig;
    private final MQClientAPIImpl mQClientAPIImpl;


    public MQClientInstance(final ClientConfig clientConfig) {
        this.nettyClientConfig = new NettyClientConfig();
        this.nettyClientConfig.setClientCallbackExecutorThreads(clientConfig.getClientCallbackExecutorThreads());
        NettyRemotingClient remotingClient = new NettyRemotingClient(this.nettyClientConfig);
        ClientRemotingProcessor clientRemotingProcessor = new ClientRemotingProcessor();
        this.mQClientAPIImpl = new MQClientAPIImpl(remotingClient, clientRemotingProcessor);
    }
}
