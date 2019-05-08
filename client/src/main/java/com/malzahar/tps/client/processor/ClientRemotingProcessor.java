package com.malzahar.tps.client.processor;

import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

public class ClientRemotingProcessor implements NettyRequestProcessor {
    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        return null;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
