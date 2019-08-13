package com.malzahar.tps.broker;

import com.malzahar.tps.common.MQVersion;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import com.malzahar.tps.remoting.netty.NettySystemConfig;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import com.malzahar.tps.store.MessageStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class BrokerStartup {

    private final static Logger logger = LoggerFactory.getLogger(BrokerStartup.class);
    public static String configFile = null;


    public static void main(String[] args) {
        start(createBrokerController(args));
    }

    public static void start(BrokerController controller) {

        try {
            controller.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BrokerController createBrokerController(String[] args) {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, Integer.toString(MQVersion.CURRENT_VERSION));

        NettySystemConfig.socketSndbufSize = 131072;
        NettySystemConfig.socketSndbufSize = 131072;

        try {

            final BrokerConfig brokerConfig = new BrokerConfig();
            final NettyServerConfig nettyServerConfig = new NettyServerConfig();
            final NettyClientConfig nettyClientConfig = new NettyClientConfig();
            final MessageStoreConfig messageStoreConfig = new MessageStoreConfig();

            InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/broker/src/main/resources/properties/broker.properties"));//todo---配置文件相对路径
            Properties properties = new Properties();
            properties.load(in);

            brokerConfig.setNamesrvAddr("127.0.0.1:3369");
            final BrokerController controller = new BrokerController(brokerConfig, nettyServerConfig, nettyClientConfig, messageStoreConfig);
            controller.registerConfig(properties);

            //namesrv地址校验 TODO

            controller.initialize();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                private volatile boolean hasShutdown = false;
                private AtomicInteger shutdownTimes = new AtomicInteger(0);

                @Override
                public void run() {
                    synchronized (this) {
                        if (!this.hasShutdown) {
                            this.hasShutdown = true;
                            long beginTime = System.currentTimeMillis();
                            controller.shutdown();
                            long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                        }
                    }
                }
            }, "ShutdownHook"));
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
