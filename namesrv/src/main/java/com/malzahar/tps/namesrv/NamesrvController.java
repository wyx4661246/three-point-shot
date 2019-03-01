package com.malzahar.tps.namesrv;

import com.malzahar.tps.common.Namesrv.NamesrvConfig;
import com.malzahar.tps.common.ThreadFactoryImpl;
import com.malzahar.tps.namesrv.processor.ServiceRegistrationProcessor;
import com.malzahar.tps.remoting.RemotingServer;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingServer;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * server端:1、连接注册，broker，cusm,pro
 * client端:
 * 定时任务:broker状态监测
 * 心跳:
 */
public class NamesrvController {

    private final static Logger log = LoggerFactory.getLogger(NamesrvController.class);

    private NettyServerConfig nettyServerConfig;

    private NamesrvConfig namesrvConfig;

    private ExecutorService remotingExecutor;

    private RemotingServer remotingServer;

    public NamesrvController(NamesrvConfig namesrvConfig, NettyServerConfig nettyServerConfig) {
        this.namesrvConfig = namesrvConfig;
        this.nettyServerConfig = nettyServerConfig;
    }

    public boolean initialize() {

        this.remotingExecutor = Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads(), new ThreadFactoryImpl("RemotingExecutorThread_"));

        remotingServer = new NettyRemotingServer(this.nettyServerConfig);
        remotingServer.registerDefaultProcessor(new ServiceRegistrationProcessor(), remotingExecutor);


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger(0);

            @Override
            public void run() {
                synchronized (this) {
                    log.info("Shutdown hook was invoked, {}", this.shutdownTimes.incrementAndGet());
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                        long beginTime = System.currentTimeMillis();
                        shutdown();
                        long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                        log.info("Shutdown hook over, consuming total time(ms): {}", consumingTimeTotal);
                    }
                }
            }
        }, "ShutdownHook"));
        return Boolean.TRUE;
    }

    public void start() throws Exception {
        remotingServer.start();
    }

    public void shutdown() {
        remotingServer.shutdown();
    }

}
