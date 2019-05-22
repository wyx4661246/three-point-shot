package com.malzahar.tps.client.impl.factory;

import com.malzahar.tps.client.config.ClientConfig;
import com.malzahar.tps.client.exception.MQClientException;
import com.malzahar.tps.client.impl.MQClientAPIImpl;
import com.malzahar.tps.client.impl.producer.MQProducerVo;
import com.malzahar.tps.client.processor.ClientRemotingProcessor;
import com.malzahar.tps.common.protocol.route.BrokerData;
import com.malzahar.tps.common.protocol.route.TopicRouteData;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * MQ客户端单例
 * 管理所有生成者消费者相关路由等信息
 * 所以使用单例
 */
public class MQClientInstance {

    private final static Logger log = LoggerFactory.getLogger(MQClientInstance.class);

    private final ConcurrentMap<String/* group */, MQProducerVo> producerTable = new ConcurrentHashMap<String, MQProducerVo>();

    private final ConcurrentMap<String/* Topic */, TopicRouteData> topicRouteTable = new ConcurrentHashMap<String, TopicRouteData>();

    /* ---------------------------------------------------------- */
    private final NettyClientConfig nettyClientConfig;
    private final MQClientAPIImpl mQClientAPIImpl;
    private Random random = new Random();

    public MQClientInstance(final ClientConfig clientConfig) {
        this.nettyClientConfig = new NettyClientConfig();
        this.nettyClientConfig.setClientCallbackExecutorThreads(clientConfig.getClientCallbackExecutorThreads());
        ClientRemotingProcessor clientRemotingProcessor = new ClientRemotingProcessor();
        this.mQClientAPIImpl = new MQClientAPIImpl(nettyClientConfig, clientRemotingProcessor, null, clientConfig);
    }

    public void start() throws MQClientException {
        mQClientAPIImpl.start();
    }


    public boolean registerProducer(final String group, final MQProducerVo producer) {
        if (null == group || null == producer) {
            return false;
        }

        MQProducerVo prev = this.producerTable.putIfAbsent(group, producer);
        if (prev != null) {
            log.warn("the producer group[{}] exist already.", group);
            return false;
        }

        return true;
    }

    public TopicRouteData getTopicRouteData(String topic) {
        try {
            TopicRouteData topicRouteData = topicRouteTable.get(topic);
            if (topicRouteData == null) {
                topicRouteData = mQClientAPIImpl.createTopic(topic, 1);
                topicRouteTable.putIfAbsent(topic, topicRouteData);
            }
            return topicRouteData;
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findBrokerAddrByTopic(final String topic) {
        TopicRouteData topicRouteData = this.getTopicRouteData(topic);
        if (topicRouteData != null) {
            List<BrokerData> brokers = topicRouteData.getBrokerDatas();
            if (!brokers.isEmpty()) {
                int index = random.nextInt(brokers.size());
                BrokerData bd = brokers.get(index % brokers.size());
                return bd.selectBrokerAddr();
            }
        }

        return null;
    }

    public BrokerData findBrokerBrokerDataByTopic(final String topic) {
        TopicRouteData topicRouteData = this.getTopicRouteData(topic);
        if (topicRouteData != null) {
            List<BrokerData> brokers = topicRouteData.getBrokerDatas();
            if (!brokers.isEmpty()) {
                int index = random.nextInt(brokers.size());
                return brokers.get(index);
            }
        }

        return null;
    }

    public MQClientAPIImpl getmQClientAPIImpl() {
        return mQClientAPIImpl;
    }
}
