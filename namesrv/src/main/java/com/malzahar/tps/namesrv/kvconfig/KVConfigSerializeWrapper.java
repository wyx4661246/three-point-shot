package com.malzahar.tps.namesrv.kvconfig;


import com.malzahar.tps.remoting.protocol.RemotingSerializable;

import java.util.HashMap;

public class KVConfigSerializeWrapper extends RemotingSerializable {
    private HashMap<String/* Namespace */, HashMap<String/* Key */, String/* Value */>> configTable;

    public HashMap<String, HashMap<String, String>> getConfigTable() {
        return configTable;
    }

    public void setConfigTable(HashMap<String, HashMap<String, String>> configTable) {
        this.configTable = configTable;
    }
}
