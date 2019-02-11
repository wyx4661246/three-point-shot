package com.malzahar.tps.broker;

import com.malzahar.tps.common.MQVersion;
import com.malzahar.tps.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class BrokerStartup {

    private final static Logger logger = LoggerFactory.getLogger(BrokerStartup.class);

    public static void main(String[] args) {
        logger.info("====================");
        start(createBrokerController(args));
    }

    public static BrokerController start(BrokerController controller) {
        return null;
    }

    public static BrokerController createBrokerController(String[] args) {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, Integer.toString(MQVersion.CURRENT_VERSION));
        final BrokerController controller = new BrokerController();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger(0);

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
    }
}
