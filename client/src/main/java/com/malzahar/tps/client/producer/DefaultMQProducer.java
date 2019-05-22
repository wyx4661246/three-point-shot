package com.malzahar.tps.client.producer;

import com.malzahar.tps.client.config.ClientConfig;
import com.malzahar.tps.client.exception.MQBrokerException;
import com.malzahar.tps.client.exception.MQClientException;
import com.malzahar.tps.client.impl.producer.DefaultMQProducerImpl;
import com.malzahar.tps.common.message.Message;
import com.malzahar.tps.remoting.RPCHook;
import com.malzahar.tps.remoting.exception.RemotingException;

import static com.malzahar.tps.common.MixAll.DEFAULT_PRODUCER_GROUP;

public class DefaultMQProducer extends ClientConfig implements MQProducer{

    protected final transient DefaultMQProducerImpl defaultMQProducerImpl;

    private String producerGroup;

    public DefaultMQProducer(){
        this(DEFAULT_PRODUCER_GROUP,null);
    }

    public DefaultMQProducer(final String producerGroup, RPCHook rpcHook) {
        this.producerGroup = producerGroup;
        this.defaultMQProducerImpl = new DefaultMQProducerImpl(this);
    }

    @Override
    public void start() throws MQClientException{
        defaultMQProducerImpl.start();
    }

    @Override
    public void send(Message message) {

        try {
            defaultMQProducerImpl.send(message,200l);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getProducerGroup() {
        return producerGroup;
    }
}
