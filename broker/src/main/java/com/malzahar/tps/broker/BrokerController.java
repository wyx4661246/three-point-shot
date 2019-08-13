package com.malzahar.tps.broker;

import com.alibaba.fastjson.JSON;
import com.malzahar.tps.broker.filtersrv.FilterServerManager;
import com.malzahar.tps.broker.longpolling.NotifyMessageArrivingListener;
import com.malzahar.tps.broker.longpolling.PullRequestHoldService;
import com.malzahar.tps.broker.out.BrokerOuterAPI;
import com.malzahar.tps.broker.topic.TopicConfigManager;
import com.malzahar.tps.common.Configuration;
import com.malzahar.tps.common.Namesrv.RegisterBrokerResult;
import com.malzahar.tps.common.TopicConfig;
import com.malzahar.tps.common.constant.PermName;
import com.malzahar.tps.common.protocol.body.TopicConfigSerializeWrapper;
import com.malzahar.tps.remoting.RemotingServer;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingClient;
import com.malzahar.tps.remoting.netty.NettyRemotingServer;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import com.malzahar.tps.store.DefaultMessageStore;
import com.malzahar.tps.store.MessageArrivingListener;
import com.malzahar.tps.store.MessageStore;
import com.malzahar.tps.store.MessageStoreConfig;
import com.malzahar.tps.store.stats.BrokerStatsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class BrokerController {

    private final static Logger logger = LoggerFactory.getLogger(BrokerController.class);

    private final BrokerOuterAPI brokerOuterAPI;
    private final FilterServerManager filterServerManager;
    private final BrokerStatsManager brokerStatsManager;
    private final MessageArrivingListener messageArrivingListener;

    private BrokerConfig brokerConfig;
    private NettyServerConfig nettyServerConfig;
    private NettyClientConfig nettyClientConfig;
    private MessageStoreConfig messageStoreConfig;
    private TopicConfigManager topicConfigManager;
    private final PullRequestHoldService pullRequestHoldService;

    private Configuration configuration;

    private RemotingServer remotingServer;
    private NettyRemotingClient remotingClient;

    private MessageStore messageStore;


    private boolean updateMasterHAServerAddrPeriodically = false;

    public BrokerController(BrokerConfig brokerConfig, NettyServerConfig nettyServerConfig, NettyClientConfig nettyClientConfig, MessageStoreConfig messageStoreConfig) {
        this.brokerConfig = brokerConfig;
        this.nettyServerConfig = nettyServerConfig;
        this.nettyClientConfig = nettyClientConfig;
        this.messageStoreConfig = messageStoreConfig;
        this.topicConfigManager = new TopicConfigManager(this);
        this.filterServerManager = new FilterServerManager(this);
        this.pullRequestHoldService = new PullRequestHoldService(this);
        this.messageArrivingListener = new NotifyMessageArrivingListener(this.pullRequestHoldService);

        this.brokerStatsManager = new BrokerStatsManager(this.brokerConfig.getBrokerClusterName());

        this.configuration = new Configuration();

        this.brokerOuterAPI = new BrokerOuterAPI(nettyClientConfig);

    }

    public boolean initialize() {

        boolean result = this.topicConfigManager.load();
        if (result) {
            try {
                this.messageStore = new DefaultMessageStore(this.messageStoreConfig, this.brokerStatsManager, this.messageArrivingListener, this.brokerConfig.isLongPollingEnable());
//                this.brokerStats = new BrokerStats((DefaultMessageStore) this.messageStore);
//                //load plugin
//                MessageStorePluginContext context = new MessageStorePluginContext(messageStoreConfig, brokerStatsManager, messageArrivingListener, brokerConfig);
//                this.messageStore = MessageStoreFactory.build(context, this.messageStore);
//                this.messageStore.getDispatcherList().addFirst(new CommitLogDispatcherCalcBitMap(this.brokerConfig, this.consumerFilterManager));
            } catch (IOException e) {
                result = false;
                logger.error("Failed to initialize", e);
            }
        }

        result = result && this.messageStore.load();

        if (result) {
            this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);
            this.remotingClient = new NettyRemotingClient(this.nettyClientConfig);
            this.registerProcessor();
            if (this.brokerConfig.getNamesrvAddr() != null) {
                this.brokerOuterAPI.updateNameServerAddressList(this.brokerConfig.getNamesrvAddr());
                logger.info("Set user specified name server address: {}", this.brokerConfig.getNamesrvAddr());
            }
        }

        return Boolean.TRUE;
    }

    public void start() throws Exception {

        if (this.messageStore != null) {
            this.messageStore.start();
        }

        if (this.remotingServer != null) {
            this.remotingServer.start();
        }

        if (this.brokerOuterAPI != null) {
            this.brokerOuterAPI.start();
        }

        if (this.pullRequestHoldService != null) {
            this.pullRequestHoldService.start();
        }

        if (this.filterServerManager != null) {
            this.filterServerManager.start();
        }

        if (this.brokerStatsManager != null) {
            this.brokerStatsManager.start();
        }

        this.registerBrokerAll(true, false, true);
    }


    public void shutdown() {
        if (this.messageStore != null) {
            this.messageStore.shutdown();
        }

        if (this.remotingServer != null) {
            this.remotingServer.shutdown();
        }

        if (this.brokerOuterAPI != null) {
            this.brokerOuterAPI.shutdown();
        }

        if (this.pullRequestHoldService != null) {
            this.pullRequestHoldService.shutdown();
        }

        if (this.filterServerManager != null) {
            this.filterServerManager.shutdown();
        }

        if (this.brokerStatsManager != null) {
            this.brokerStatsManager.shutdown();
        }
    }

    public void registerProcessor() {

    }

    public void registerConfig(Properties extProperties) {
        configuration.registerConfig(extProperties);
    }

    public String getBrokerAddr() {
        return this.brokerConfig.getBrokerIP1() + ":" + this.nettyServerConfig.getListenPort();
    }

    public BrokerConfig getBrokerConfig() {
        return brokerConfig;
    }

    public MessageStoreConfig getMessageStoreConfig() {
        return messageStoreConfig;
    }

    public TopicConfigManager getTopicConfigManager() {
        return topicConfigManager;
    }


    public synchronized void registerBrokerAll(final boolean checkOrderConfig, boolean oneway, boolean forceRegister) {
        TopicConfigSerializeWrapper topicConfigWrapper = this.getTopicConfigManager().buildTopicConfigSerializeWrapper();

        if (!PermName.isWriteable(this.getBrokerConfig().getBrokerPermission())
                || !PermName.isReadable(this.getBrokerConfig().getBrokerPermission())) {
            ConcurrentHashMap<String, TopicConfig> topicConfigTable = new ConcurrentHashMap<String, TopicConfig>();
            for (TopicConfig topicConfig : topicConfigWrapper.getTopicConfigTable().values()) {

                TopicConfig tmp = new TopicConfig(topicConfig.getTopicName(), topicConfig.getReadQueueNums(), topicConfig.getWriteQueueNums(), this.brokerConfig.getBrokerPermission());

                topicConfigTable.put(topicConfig.getTopicName(), tmp);
            }
            topicConfigWrapper.setTopicConfigTable(topicConfigTable);
        }

        if (forceRegister || needRegister(this.brokerConfig.getBrokerClusterName(),
                this.getBrokerAddr(),
                this.brokerConfig.getBrokerName(),
                this.brokerConfig.getBrokerId(),
                this.brokerConfig.getRegisterBrokerTimeoutMills())) {
            doRegisterBrokerAll(checkOrderConfig, oneway, topicConfigWrapper);
        }
    }

    private void doRegisterBrokerAll(boolean checkOrderConfig, boolean oneway,
                                     TopicConfigSerializeWrapper topicConfigWrapper) {
        List<RegisterBrokerResult> registerBrokerResultList = this.brokerOuterAPI.registerBrokerAll(
                this.brokerConfig.getBrokerClusterName(),
                this.getBrokerAddr(),
                this.brokerConfig.getBrokerName(),
                this.brokerConfig.getBrokerId(),
                this.getHAServerAddr(),
                topicConfigWrapper,
                this.filterServerManager.buildNewFilterServerList(),
                oneway,
                this.brokerConfig.getRegisterBrokerTimeoutMills(),
                this.brokerConfig.isCompressedRegister());

        if (registerBrokerResultList.size() > 0) {
            RegisterBrokerResult registerBrokerResult = registerBrokerResultList.get(0);
            if (registerBrokerResult != null) {
                if (this.updateMasterHAServerAddrPeriodically && registerBrokerResult.getHaServerAddr() != null) {
                    this.messageStore.updateHaMasterAddress(registerBrokerResult.getHaServerAddr());
                }

                //this.slaveSynchronize.setMasterAddr(registerBrokerResult.getMasterAddr());

                if (checkOrderConfig) {
                    this.getTopicConfigManager().updateOrderTopicConfig(registerBrokerResult.getKvTable());
                }
            }
        }
    }

    public String getHAServerAddr() {
        return this.brokerConfig.getBrokerIP2() + ":" + this.messageStoreConfig.getHaListenPort();
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    private boolean needRegister(final String clusterName,
                                 final String brokerAddr,
                                 final String brokerName,
                                 final long brokerId,
                                 final int timeoutMills) {

        TopicConfigSerializeWrapper topicConfigWrapper = this.getTopicConfigManager().buildTopicConfigSerializeWrapper();
        List<Boolean> changeList = brokerOuterAPI.needRegister(clusterName, brokerAddr, brokerName, brokerId, topicConfigWrapper, timeoutMills);
        boolean needRegister = false;
        for (Boolean changed : changeList) {
            if (changed) {
                needRegister = true;
                break;
            }
        }
        return needRegister;
    }
}
