package com.malzahar.tps.namesrv;

import com.malzahar.tps.common.Namesrv.NamesrvConfig;
import com.malzahar.tps.common.ThreadFactoryImpl;
import com.malzahar.tps.common.protocol.RequestCode;
import com.malzahar.tps.namesrv.kvconfig.KVConfigManager;
import com.malzahar.tps.namesrv.processor.DefaultRequestProcessor;
import com.malzahar.tps.namesrv.processor.GetRountByTopicProcessor;
import com.malzahar.tps.namesrv.processor.QueryAndCreateTopicProcessor;
import com.malzahar.tps.namesrv.processor.ServiceRegistrationProcessor;
import com.malzahar.tps.namesrv.routeInfoManager.RouteInfoManager;
import com.malzahar.tps.remoting.RemotingClient;
import com.malzahar.tps.remoting.RemotingServer;
import com.malzahar.tps.remoting.netty.NettyClientConfig;
import com.malzahar.tps.remoting.netty.NettyRemotingClient;
import com.malzahar.tps.remoting.netty.NettyRemotingServer;
import com.malzahar.tps.remoting.netty.NettyServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * server端:1、连接注册，broker，cusm,  pro
 * client端:
 * 定时任务:broker状态监测
 * 心跳:
 */
public class NamesrvController {

    private final static Logger log = LoggerFactory.getLogger(NamesrvController.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("NameSvrScheduledThread"));

    private NettyServerConfig nettyServerConfig;

    private RemotingClient remotingClient;

    private NamesrvConfig namesrvConfig;

    private ExecutorService remotingExecutor;

    private RemotingServer remotingServer;

    private final RouteInfoManager routeInfoManager;

    private final KVConfigManager kvConfigManager;

    public NamesrvController(NamesrvConfig namesrvConfig, NettyServerConfig nettyServerConfig) {
        this.namesrvConfig = namesrvConfig;
        this.nettyServerConfig = nettyServerConfig;
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        nettyClientConfig.setClientCallbackExecutorThreads(namesrvConfig.getClientCallbackExecutorThreads());
        this.remotingClient = new NettyRemotingClient(nettyClientConfig, null);
        this.routeInfoManager = new RouteInfoManager();
        this.kvConfigManager = new KVConfigManager(this);
    }

    public boolean initialize() {

        this.remotingExecutor = Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads(), new ThreadFactoryImpl("RemotingExecutorThread_"));

        remotingServer = new NettyRemotingServer(this.nettyServerConfig);
        registerProcessor();

        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                NamesrvController.this.routeInfoManager.scanNotActiveBroker();
            }
        }, 5, 10, TimeUnit.SECONDS);


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
        remotingClient.start();
    }

    public void shutdown() {
        remotingServer.shutdown();
    }

    private void registerProcessor() {

        this.remotingServer.registerDefaultProcessor(new DefaultRequestProcessor(this), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.ADD_BROKER, new ServiceRegistrationProcessor(this), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.GET_ROUTEINTO_BY_TOPIC_, new GetRountByTopicProcessor(this), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.QUERY_AND_CREATE_TOPIC_, new QueryAndCreateTopicProcessor(this), this.remotingExecutor);
    }

    public RouteInfoManager getRouteInfoManager() {
        return routeInfoManager;
    }

    public NamesrvConfig getNamesrvConfig() {
        return namesrvConfig;
    }

    public KVConfigManager getKvConfigManager() {
        return kvConfigManager;
    }

    public RemotingClient getRemotingClient() {
        return remotingClient;
    }
}
