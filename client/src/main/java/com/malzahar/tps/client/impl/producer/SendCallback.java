package com.malzahar.tps.client.impl.producer;

import com.malzahar.tps.client.producer.SendResult;

public interface SendCallback {
    void onSuccess(final SendResult sendResult);

    void onException(final Throwable e);
}
