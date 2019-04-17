package com.malzahar.tps.namesrv.processor;

import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.namesrv.NamesrvController;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.ResponseCode;
import com.malzahar.tps.remoting.protocol.header.namesrv.RegisterBrokerRequestHeader;
import com.malzahar.tps.remoting.protocol.header.namesrv.RegisterBrokerResponseHeader;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegistrationProcessor implements NettyRequestProcessor {

    private final static Logger log = LoggerFactory.getLogger(ServiceRegistrationProcessor.class);

    protected final NamesrvController namesrvController;

    public ServiceRegistrationProcessor(NamesrvController namesrvController) {
        this.namesrvController = namesrvController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        if(null == ctx){

        }
        final RemotingCommand response = RemotingCommand.createResponseCommand(RegisterBrokerResponseHeader.class);
        final RegisterBrokerResponseHeader responseHeader = (RegisterBrokerResponseHeader) response.readCustomHeader();
        final RegisterBrokerRequestHeader requestHeader = (RegisterBrokerRequestHeader) request.decodeCommandCustomHeader(RegisterBrokerRequestHeader.class);

        log.info("========test========{}",requestHeader.getBrokerAddr());



        byte[] jsonValue = "".getBytes();
        response.setBody(jsonValue);
        response.setCode(ResponseCode.SUCCESS);
        response.setRemark(null);
        return response;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
