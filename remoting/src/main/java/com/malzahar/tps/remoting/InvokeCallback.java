package com.malzahar.tps.remoting;


import com.malzahar.tps.remoting.netty.ResponseFuture;

public interface InvokeCallback {
    void operationComplete(final ResponseFuture responseFuture);
}
