package com.malzahar.tps.namesrv.processor;

import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.namesrv.NamesrvController;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

public class DefaultRequestProcessor implements NettyRequestProcessor {


    protected final NamesrvController namesrvController;

    public DefaultRequestProcessor(NamesrvController namesrvController) {
        this.namesrvController = namesrvController;
    }

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
