
package com.malzahar.tps.remoting;

public interface RemotingService {
    void start();

    void shutdown();

    void registerRPCHook(RPCHook rpcHook);
}
