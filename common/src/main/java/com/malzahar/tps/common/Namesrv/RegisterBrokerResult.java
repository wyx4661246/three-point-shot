package com.malzahar.tps.common.Namesrv;


import com.malzahar.tps.common.protocol.body.KVTable;

public class RegisterBrokerResult {
    private String haServerAddr;
    private String masterAddr;
    private KVTable kvTable;

    public String getHaServerAddr() {
        return haServerAddr;
    }

    public void setHaServerAddr(String haServerAddr) {
        this.haServerAddr = haServerAddr;
    }

    public String getMasterAddr() {
        return masterAddr;
    }

    public void setMasterAddr(String masterAddr) {
        this.masterAddr = masterAddr;
    }

    public KVTable getKvTable() {
        return kvTable;
    }

    public void setKvTable(KVTable kvTable) {
        this.kvTable = kvTable;
    }
}
