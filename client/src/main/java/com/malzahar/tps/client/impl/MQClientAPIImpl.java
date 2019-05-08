package com.malzahar.tps.client.impl;

import com.malzahar.tps.client.exception.MQBrokerException;
import com.malzahar.tps.client.processor.ClientRemotingProcessor;
import com.malzahar.tps.client.producer.SendResult;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.remoting.RemotingClient;
import com.malzahar.tps.remoting.exception.RemotingException;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.sun.deploy.config.ClientConfig;

public class MQClientAPIImpl {
    private final RemotingClient remotingClient;
    private final ClientRemotingProcessor clientRemotingProcessor;
    private String nameSrvAddr = null;
    private ClientConfig clientConfig;

    public MQClientAPIImpl(RemotingClient remotingClient, ClientRemotingProcessor clientRemotingProcessor) {
        this.remotingClient = remotingClient;
        this.clientRemotingProcessor = clientRemotingProcessor;
    }



    private SendResult sendMessageSync(
            final String addr,
            final String brokerName,
            final Message msg,
            final long timeoutMillis,
            final RemotingCommand request
    ) throws RemotingException, MQBrokerException, InterruptedException {
        RemotingCommand response = this.remotingClient.invokeSync(addr, request, timeoutMillis);
        assert response != null;
        return new SendResult();
    }


}
