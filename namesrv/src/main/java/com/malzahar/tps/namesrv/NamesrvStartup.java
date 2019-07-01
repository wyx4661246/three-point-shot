package com.malzahar.tps.namesrv;

import com.malzahar.tps.common.Namesrv.NamesrvConfig;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamesrvStartup {

    private final static Logger log = LoggerFactory.getLogger(NamesrvController.class);

    public static void main(String[] args) {
        start(createBrokerController(args));
    }

    public static NamesrvController createBrokerController(String[] args) {
        final NamesrvConfig namesrvConfig = new NamesrvConfig();
        final NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setListenPort(3369);
        NamesrvController namesrvController = new NamesrvController(namesrvConfig, nettyServerConfig);
        return namesrvController;
    }

    public static void start(NamesrvController namesrvController) {
        try {
            namesrvController.initialize();
            namesrvController.start();
        } catch (Exception e) {
            System.exit(-1);
        }

    }
}
