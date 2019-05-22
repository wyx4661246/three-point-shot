package com.malzahar.tps.client.impl;

import com.malzahar.tps.client.config.ClientConfig;
import com.malzahar.tps.client.exception.MQBrokerException;
import com.malzahar.tps.client.exception.MQClientException;
import com.malzahar.tps.client.processor.ClientRemotingProcessor;
import com.malzahar.tps.client.producer.SendResult;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.common.protocol.route.TopicRouteData;
import com.malzahar.tps.remoting.RPCHook;
import com.malzahar.tps.remoting.RemotingClient;
import com.malzahar.tps.remoting.exception.RemotingConnectException;
import com.malzahar.tps.remoting.exception.RemotingException;
import com.malzahar.tps.remoting.exception.RemotingSendRequestException;
import com.malzahar.tps.remoting.exception.RemotingTimeoutException;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingClient;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.ResponseCode;
import com.malzahar.tps.remoting.protocol.header.namesrv.CreateTopicRequestHeader;

public class MQClientAPIImpl {

    private final RemotingClient remotingClient;
    private final ClientRemotingProcessor clientRemotingProcessor;
    private String nameSrvAddr = null;
    private ClientConfig clientConfig;

    public MQClientAPIImpl(final NettyClientConfig nettyClientConfig,
                           final ClientRemotingProcessor clientRemotingProcessor,
                           RPCHook rpcHook, final ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.clientRemotingProcessor = clientRemotingProcessor;

        this.remotingClient = new NettyRemotingClient(nettyClientConfig, null);
        this.remotingClient.registerRPCHook(rpcHook);
        this.remotingClient.registerProcessor(RequestCode.CHECK_TRANSACTION_STATE, this.clientRemotingProcessor, null);
    }

    public void start() {
        this.remotingClient.start();
    }

    public void shutdown() {
        this.remotingClient.shutdown();
    }


    public TopicRouteData createTopic(String newTopic, int queueNum) throws MQClientException {
        try {
            CreateTopicRequestHeader requestHeader = new CreateTopicRequestHeader();
            requestHeader.setTopic(newTopic);
            requestHeader.setDefaultTopic(newTopic);
            requestHeader.setReadQueueNums(queueNum);
            requestHeader.setWriteQueueNums(queueNum);
            requestHeader.setPerm(null);
            requestHeader.setTopicFilterType(null);
            requestHeader.setTopicSysFlag(null);
            requestHeader.setOrder(Boolean.FALSE);

            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.QUERY_AND_CREATE_TOPIC_, requestHeader);

            RemotingCommand response = this.remotingClient.invokeSync(clientConfig.getNamesrvAddr(), request, 10000l);

            switch (response.getCode()) {
                case ResponseCode.TOPIC_NOT_EXIST: {
                    break;
                }
                case ResponseCode.SUCCESS: {
                    byte[] body = response.getBody();
                    if (body != null) {
                        return TopicRouteData.decode(body, TopicRouteData.class);
                    }
                }
                default:
                    break;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingConnectException e) {
            e.printStackTrace();
        } catch (RemotingSendRequestException e) {
            e.printStackTrace();
        } catch (RemotingTimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SendResult sendMessageSync(
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
