package com.malzahar.tps.remoting;

import com.malzahar.tps.remoting.protocol.RemotingCommand;

public interface RPCHook {

    void doBeforeRequest(final String remoteAddr, final RemotingCommand request);

    void doAfterResponse(final String remoteAddr, final RemotingCommand request,
                         final RemotingCommand response);
}
