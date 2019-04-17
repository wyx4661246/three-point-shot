package com.malzahar.tps.remoting.protocol.header.namesrv;


import com.malzahar.tps.remoting.CommandCustomHeader;
import com.malzahar.tps.remoting.exception.RemotingCommandException;

public class RegisterBrokerRequestHeader implements CommandCustomHeader {
    private String brokerName;
    private String brokerAddr;
    private String clusterName;
    private String haServerAddr;
    private Long brokerId;

    private boolean compressed;

    private Integer bodyCrc32 = 0;

    public void checkFields() throws RemotingCommandException {
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerAddr() {
        return brokerAddr;
    }

    public void setBrokerAddr(String brokerAddr) {
        this.brokerAddr = brokerAddr;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHaServerAddr() {
        return haServerAddr;
    }

    public void setHaServerAddr(String haServerAddr) {
        this.haServerAddr = haServerAddr;
    }

    public Long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public Integer getBodyCrc32() {
        return bodyCrc32;
    }

    public void setBodyCrc32(Integer bodyCrc32) {
        this.bodyCrc32 = bodyCrc32;
    }
}
