package com.malzahar.tps.client.impl.producer;

import com.malzahar.tps.client.exception.MQBrokerException;
import com.malzahar.tps.client.exception.MQClientException;
import com.malzahar.tps.client.impl.CommunicationMode;
import com.malzahar.tps.client.impl.MQClientAPIImpl;
import com.malzahar.tps.client.impl.factory.MQClientInstance;
import com.malzahar.tps.client.producer.DefaultMQProducer;
import com.malzahar.tps.client.producer.SendResult;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.remoting.exception.RemotingException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultMQProducerImpl {

    private final DefaultMQProducer defaultMQProducer;
    private MQClientInstance mQClientFactory;

    public DefaultMQProducerImpl(final DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    public void start() {
        this.mQClientFactory = new MQClientInstance();
    }

    public SendResult send(Message msg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.sendDefaultImpl(msg, CommunicationMode.SYNC, null, timeout);
    }

    private SendResult sendDefaultImpl(final Message msg, final CommunicationMode sync, final SendCallback sendCallback, final long timeout) {
    }
}
