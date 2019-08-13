package com.malzahar.tps.remoting.protocol.header.namesrv;


import com.malzahar.tps.remoting.CommandCustomHeader;
import com.malzahar.tps.remoting.exception.RemotingCommandException;

public class QueryDataVersionRequestHeader implements CommandCustomHeader {
    private String brokerName;
    private String brokerAddr;
    private String clusterName;
    private Long brokerId;

    @Override
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

    public Long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }
}
