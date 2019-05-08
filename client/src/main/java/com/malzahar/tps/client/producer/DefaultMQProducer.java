package com.malzahar.tps.client.producer;

import com.malzahar.tps.client.config.ClientConfig;
import com.malzahar.tps.client.impl.producer.DefaultMQProducerImpl;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.remoting.RPCHook;

import static com.malzahar.tps.common.MixAll.DEFAULT_PRODUCER_GROUP;

public class DefaultMQProducer extends ClientConfig implements MQProducer{

    protected final transient DefaultMQProducerImpl defaultMQProducerImpl;

    private String producerGroup;

    public DefaultMQProducer(){
        this(DEFAULT_PRODUCER_GROUP,null);
    }

    public DefaultMQProducer(final String producerGroup, RPCHook rpcHook) {
        this.producerGroup = producerGroup;
        defaultMQProducerImpl = new DefaultMQProducerImpl();
    }

    @Override
    public void start(){

    }

    @Override
    public void send(Message message) {

    }
}
