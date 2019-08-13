package com.malzahar.tps.broker.processor;

import com.malzahar.tps.broker.BrokerController;
import com.malzahar.tps.common.TopicConfig;
import com.malzahar.tps.remoting.common.RemotingHelper;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.ResponseCode;
import com.malzahar.tps.remoting.protocol.header.namesrv.CreateTopicRequestHeader;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateAndCreateTopicProcessor implements NettyRequestProcessor {

    private final static Logger log = LoggerFactory.getLogger(UpdateAndCreateTopicProcessor.class);

    private final BrokerController brokerController;

    public UpdateAndCreateTopicProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        final RemotingCommand response = RemotingCommand.createResponseCommand(null);
        final CreateTopicRequestHeader requestHeader = (CreateTopicRequestHeader) request.decodeCommandCustomHeader(CreateTopicRequestHeader.class);
        log.info("updateAndCreateTopic called by {}", RemotingHelper.parseChannelRemoteAddr(ctx.channel()));

        if (requestHeader.getTopic().equals(this.brokerController.getBrokerConfig().getBrokerClusterName())) {
            String errorMsg = "the topic[" + requestHeader.getTopic() + "] is conflict with system reserved words.";
            log.warn("the topic[{}] is conflict with system reserved words.",requestHeader.getTopic());
            response.setCode(ResponseCode.SYSTEM_ERROR);
            response.setRemark(errorMsg);
            return response;
        }

        try {
            response.setCode(ResponseCode.SUCCESS);
            response.setOpaque(request.getOpaque());
            response.markResponseType();
            response.setRemark(null);
            ctx.writeAndFlush(response);
        } catch (Exception e) {
            log.error("Failed to produce a proper response", e);
        }

        TopicConfig topicConfig = new TopicConfig(requestHeader.getTopic());
        topicConfig.setReadQueueNums(requestHeader.getReadQueueNums());
        topicConfig.setWriteQueueNums(requestHeader.getWriteQueueNums());
        topicConfig.setTopicFilterType(requestHeader.getTopicFilterType());
        topicConfig.setPerm(requestHeader.getPerm());
        topicConfig.setTopicSysFlag(requestHeader.getTopicSysFlag() == null ? 0 : requestHeader.getTopicSysFlag());

        this.brokerController.getTopicConfigManager().updateTopicConfig(topicConfig);

        //this.brokerController.registerIncrementBrokerData(topicConfig,this.brokerController.getTopicConfigManager().getDataVersion());


        return null;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
