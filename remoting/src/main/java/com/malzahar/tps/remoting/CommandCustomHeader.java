package com.malzahar.tps.remoting;


import com.malzahar.tps.remoting.exception.RemotingCommandException;

public interface CommandCustomHeader {
    void checkFields() throws RemotingCommandException;
}
