package com.malzahar.tps.remoting.protocol.header.namesrv;


import com.malzahar.tps.remoting.CommandCustomHeader;
import com.malzahar.tps.remoting.exception.RemotingCommandException;

public class GetRouteInfoRequestHeader implements CommandCustomHeader {
    private String topic;

    @Override
    public void checkFields() throws RemotingCommandException {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
