package com.malzahar.tps.broker.routeinfo;

import com.malzahar.tps.broker.entity.BrokerLiveInfo;

import java.util.HashMap;
import java.util.List;

public class RouteInfoManager {

    private final HashMap<String/* brokerAddr */, BrokerLiveInfo> brokerLiveTable;
    private final HashMap<String/* brokerAddr */, List<String>/* Filter Server */> filterServerTable;


    public RouteInfoManager() {
        this.brokerLiveTable = new HashMap<String, BrokerLiveInfo>(256);
        this.filterServerTable = new HashMap<String, List<String>>(256);
    }


    public Boolean registerBroker(String brokerAddr, BrokerLiveInfo brokerLiveInfo) {
        brokerLiveTable.put(brokerAddr, brokerLiveInfo);
        return Boolean.TRUE;
    }
}
