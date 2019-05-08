package com.malzahar.tps.client.config;

import com.malzahar.tps.common.MixAll;
import com.malzahar.tps.common.RemotingUtil;
import com.malzahar.tps.common.UtilAll;
import com.malzahar.tps.remoting.protocol.LanguageCode;

public class ClientConfig {

    private String unitName;
    private String instanceName = System.getProperty("mq.client.name", "DEFAULT");
    private int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    /**
     * 注册中心地址
     */
    private String namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY, System.getenv(MixAll.NAMESRV_ADDR_ENV));

    /**
     * ip地址
     */
    private String clientIP = RemotingUtil.getLocalAddress();

    /**
     * 与namesvr拉取topic信息间隔
     */
    private int pollNameServerInterval = 1000 * 30;
    /**
     * 与broker心跳间隔
     */
    private int heartbeatBrokerInterval = 1000 * 30;
    /**
     * 与消费者偏移量间隔
     *
     */
    private int persistConsumerOffsetInterval = 1000 * 5;

    private LanguageCode language = LanguageCode.JAVA;


    public String buildMQClientId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClientIP());

        sb.append("@");
        sb.append(this.getInstanceName());
        if (!UtilAll.isBlank(this.unitName)) {
            sb.append("@");
            sb.append(this.unitName);
        }

        return sb.toString();
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public int getPollNameServerInterval() {
        return pollNameServerInterval;
    }

    public void setPollNameServerInterval(int pollNameServerInterval) {
        this.pollNameServerInterval = pollNameServerInterval;
    }

    public int getHeartbeatBrokerInterval() {
        return heartbeatBrokerInterval;
    }

    public void setHeartbeatBrokerInterval(int heartbeatBrokerInterval) {
        this.heartbeatBrokerInterval = heartbeatBrokerInterval;
    }

    public int getPersistConsumerOffsetInterval() {
        return persistConsumerOffsetInterval;
    }

    public void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
        this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
    }

    public LanguageCode getLanguage() {
        return language;
    }

    public void setLanguage(LanguageCode language) {
        this.language = language;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }
}
