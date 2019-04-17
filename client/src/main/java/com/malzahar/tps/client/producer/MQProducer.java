package com.malzahar.tps.client.producer;

import com.malzahar.tps.client.MQAdmin;
import com.malzahar.tps.common.message.Message;

public interface MQProducer extends MQAdmin {

    void send(Message message);
}
