package com.malzahar.tps.namesrv.processor;

import com.malzahar.tps.common.help.FAQUrl;
import com.malzahar.tps.common.protocol.route.TopicRouteData;
import com.malzahar.tps.namesrv.NamesrvController;
import com.malzahar.tps.namesrv.util.NamesrvUtil;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.ResponseCode;
import com.malzahar.tps.remoting.protocol.header.namesrv.GetRouteInfoRequestHeader;
import com.malzahar.tps.remoting.protocol.header.namesrv.RegisterBrokerRequestHeader;
import com.malzahar.tps.remoting.protocol.header.namesrv.RegisterBrokerResponseHeader;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRountByTopicProcessor implements NettyRequestProcessor {

    private final static Logger log = LoggerFactory.getLogger(GetRountByTopicProcessor.class);

    protected final NamesrvController namesrvController;

    public GetRountByTopicProcessor(NamesrvController namesrvController) {
        this.namesrvController = namesrvController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        if (null == ctx) {

        }
        final RemotingCommand response = RemotingCommand.createResponseCommand(null);
        final GetRouteInfoRequestHeader requestHeader = (GetRouteInfoRequestHeader) request.decodeCommandCustomHeader(GetRouteInfoRequestHeader.class);

        TopicRouteData topicRouteData = this.namesrvController.getRouteInfoManager().pickupTopicRouteData(requestHeader.getTopic());

        if (topicRouteData != null) {
            if (this.namesrvController.getNamesrvConfig().isOrderMessageEnable()) {
                String orderTopicConf = this.namesrvController.getKvConfigManager()
                        .getKVConfig(NamesrvUtil.NAMESPACE_ORDER_TOPIC_CONFIG, requestHeader.getTopic());
                topicRouteData.setOrderTopicConf(orderTopicConf);
            }

            byte[] content = topicRouteData.encode();
            response.setBody(content);
            response.setCode(ResponseCode.SUCCESS);
            response.setRemark(null);
            return response;
        }
        response.setCode(ResponseCode.TOPIC_NOT_EXIST);
        response.setRemark("No topic route info in name server for the topic: " + requestHeader.getTopic()
                + FAQUrl.suggestTodo(FAQUrl.APPLY_TOPIC_URL));
        return response;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
