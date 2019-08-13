package com.malzahar.tps.remoting.protocol.header.namesrv;


import com.malzahar.tps.remoting.CommandCustomHeader;
import com.malzahar.tps.remoting.exception.RemotingCommandException;

public class QueryDataVersionResponseHeader implements CommandCustomHeader {
    private Boolean changed;

    @Override
    public void checkFields() throws RemotingCommandException {

    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryDataVersionResponseHeader{");
        sb.append("changed=").append(changed);
        sb.append('}');
        return sb.toString();
    }
}
