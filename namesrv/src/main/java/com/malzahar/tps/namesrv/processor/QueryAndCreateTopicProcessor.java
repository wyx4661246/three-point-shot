package com.malzahar.tps.namesrv.processor;

import com.malzahar.tps.common.help.FAQUrl;
import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.common.protocol.route.TopicRouteData;
import com.malzahar.tps.namesrv.NamesrvController;
import com.malzahar.tps.namesrv.util.NamesrvUtil;
import com.malzahar.tps.remoting.netty.NettyRequestProcessor;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.remoting.protocol.ResponseCode;
import com.malzahar.tps.remoting.protocol.header.namesrv.CreateTopicRequestHeader;
import com.malzahar.tps.remoting.protocol.header.namesrv.GetRouteInfoRequestHeader;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QueryAndCreateTopicProcessor implements NettyRequestProcessor {

    private final static Logger log = LoggerFactory.getLogger(QueryAndCreateTopicProcessor.class);

    protected final NamesrvController namesrvController;

    public QueryAndCreateTopicProcessor(NamesrvController namesrvController) {
        this.namesrvController = namesrvController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        if (null == ctx) {

        }
        final RemotingCommand response = RemotingCommand.createResponseCommand(null);
        final CreateTopicRequestHeader requestHeader = (CreateTopicRequestHeader) request.decodeCommandCustomHeader(GetRouteInfoRequestHeader.class);

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

        List<String> activeBrokerAdds = this.namesrvController.getRouteInfoManager().getActiveBroker();
        if (activeBrokerAdds == null || activeBrokerAdds.isEmpty()) {
            response.setCode(ResponseCode.TOPIC_NOT_EXIST);
            response.setRemark("No topic route info in name server for the topic: " + requestHeader.getTopic()
                    + FAQUrl.suggestTodo(FAQUrl.APPLY_TOPIC_URL));
        }
        for (String activeBrokerAdd : activeBrokerAdds) {
            CreateTopicRequestHeader createTopicRequestHeader = new CreateTopicRequestHeader();
            createTopicRequestHeader.setTopic(requestHeader.getTopic());
            createTopicRequestHeader.setDefaultTopic(requestHeader.getTopic());
            createTopicRequestHeader.setReadQueueNums(requestHeader.getReadQueueNums());
            createTopicRequestHeader.setWriteQueueNums(requestHeader.getWriteQueueNums());
            createTopicRequestHeader.setPerm(requestHeader.getPerm());
            createTopicRequestHeader.setTopicFilterType(requestHeader.getTopicFilterType());
            createTopicRequestHeader.setTopicSysFlag(requestHeader.getTopicSysFlag());
            createTopicRequestHeader.setOrder(requestHeader.getOrder());

            RemotingCommand requestCreateTopic = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC, createTopicRequestHeader);
            namesrvController.getRemotingClient().invokeAsync(activeBrokerAdd, requestCreateTopic, 1000l, null);
        }


        topicRouteData = new TopicRouteData();

        byte[] content = topicRouteData.encode();
        response.setBody(content);
        response.setCode(ResponseCode.SUCCESS);
        response.setRemark(null);
        return response;

    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
