package com.malzahar.tps.remoting;


import com.malzahar.tps.remoting.common.Pair;
import com.malzahar.tps.remoting.exception.RemotingSendRequestException;
import com.malzahar.tps.remoting.exception.RemotingTimeoutException;
import com.malzahar.tps.remoting.exception.RemotingTooMuchRequestException;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;

public interface RemotingServer extends RemotingService {

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);

    int localListenPort();

    Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(final int requestCode);

    RemotingCommand invokeSync(final Channel channel, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;

    void invokeAsync(final Channel channel, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback) throws InterruptedException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;

    void invokeOneway(final Channel channel, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException,
            RemotingSendRequestException;



}
