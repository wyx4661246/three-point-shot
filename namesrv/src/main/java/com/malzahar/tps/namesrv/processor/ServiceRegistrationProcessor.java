package com.malzahar.tps.namesrv.processor;

import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

public class ServiceRegistrationProcessor implements NettyRequestProcessor {


    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        if(null == ctx){

        }
        switch (request.getCode()) {
            case RequestCode.ADD_BROKER:
                return null;
        }
        return null;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
