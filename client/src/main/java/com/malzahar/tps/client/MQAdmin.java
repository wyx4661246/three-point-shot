package com.malzahar.tps.client;

import com.malzahar.tps.client.exception.MQClientException;

public interface MQAdmin {
    void start() throws MQClientException;
}
