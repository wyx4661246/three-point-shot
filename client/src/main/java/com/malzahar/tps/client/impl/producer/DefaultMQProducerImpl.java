package com.malzahar.tps.client.impl.producer;

import com.malzahar.tps.client.exception.MQBrokerException;
import com.malzahar.tps.client.exception.MQClientException;
import com.malzahar.tps.client.impl.CommunicationMode;
import com.malzahar.tps.client.impl.MQClientManager;
import com.malzahar.tps.client.impl.factory.MQClientInstance;
import com.malzahar.tps.client.producer.DefaultMQProducer;
import com.malzahar.tps.client.producer.SendResult;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.common.message.MessageBatch;
import com.malzahar.tps.common.message.MessageDecoder;
import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.common.protocol.route.BrokerData;
import com.malzahar.tps.common.protocol.route.TopicRouteData;
import com.malzahar.tps.remoting.exception.RemotingException;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.header.SendMessageRequestHeader;

import java.util.List;


public class DefaultMQProducerImpl {

    private final DefaultMQProducer defaultMQProducer;
    private MQClientInstance mQClientInstance;

    public DefaultMQProducerImpl(final DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    public void start() throws MQClientException {
        this.mQClientInstance = MQClientManager.getInstance().getAndCreateMQClientInstance(defaultMQProducer);
        mQClientInstance.start();
        boolean registerOK = mQClientInstance.registerProducer(this.defaultMQProducer.getProducerGroup(), null);
    }

    public SendResult send(Message msg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {

        return this.sendDefaultImpl(msg, CommunicationMode.SYNC, null, timeout);
    }

    private SendResult sendDefaultImpl(final Message msg, final CommunicationMode sync, final SendCallback sendCallback, final long timeout) {
        BrokerData brokerData = mQClientInstance.findBrokerBrokerDataByTopic(msg.getTopic());
        if (brokerData == null) {
        }
        SendMessageRequestHeader requestHeader = new SendMessageRequestHeader();
        requestHeader.setProducerGroup(this.defaultMQProducer.getProducerGroup());
        requestHeader.setTopic(msg.getTopic());
        requestHeader.setDefaultTopic(null);
        requestHeader.setDefaultTopicQueueNums(null);
        requestHeader.setQueueId(null);
        requestHeader.setSysFlag(null);
        requestHeader.setBornTimestamp(System.currentTimeMillis());
        requestHeader.setFlag(msg.getFlag());
        requestHeader.setProperties(MessageDecoder.messageProperties2String(msg.getProperties()));
        requestHeader.setReconsumeTimes(0);
        requestHeader.setUnitMode(false);
        requestHeader.setBatch(msg instanceof MessageBatch);
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE_, requestHeader);
        SendResult sendResult = null;
        try {
            sendResult = mQClientInstance.getmQClientAPIImpl().sendMessageSync(brokerData.selectBrokerAddr(), brokerData.getBrokerName(), msg, timeout, request);
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sendResult;
    }
}
