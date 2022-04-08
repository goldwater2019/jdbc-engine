package com.ane56.engine.jdbc.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class ZkUtils {
    private volatile static ZkUtils singleton;
    private String connectionStr;
    private CuratorFramework client;
    private volatile boolean isRunning;

    private ZkUtils(String connectionStr) {
        setConnectionStr(connectionStr);
        client = CuratorFrameworkFactory.builder()
                .connectString("bdtnode10:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        isRunning = false;
    }

    public static ZkUtils getInstance(String connectionStr) {
        if (singleton == null) {
            synchronized (ZkUtils.class) {
                if (singleton == null) {
                    singleton = new ZkUtils(connectionStr);
                }
            }
        }
        return singleton;
    }

    /**
     * 修改running的状态
     *
     * @param isRunning
     */
    public synchronized void changeRunningStatus(boolean isRunning) {
        setRunningStatus(isRunning);
    }

    private void setRunningStatus(boolean isRunning) {
        boolean originalStatus = isRunning();
        if (!originalStatus && isRunning) {
            start();
            setRunning(true);
        }
        if (originalStatus && !isRunning) {
            close();
            setRunning(false);
        }
    }

    private void start() {
        client.start();
    }

    private void close() {
        client.close();
    }

    public void createEphemeralSequentialNode(String zNodePath, String message) throws Exception {
        changeRunningStatus(true);
        client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(zNodePath, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 返回 (zNodePath, Data)
     *
     * @param parentPath
     * @return
     * @throws Exception
     */
    public Map<String, String> getDataWithEphemeralSequentialNodes(String parentPath) throws Exception {
        changeRunningStatus(true);
        Map<String, String> result = new HashMap<>();
        List<String> strings = client.getChildren().forPath(parentPath);
        for (String string : strings) {
            String zNodePath = String.join("/", parentPath, string);
            byte[] bytes = client.getData().forPath(zNodePath);
            result.put(zNodePath, new String(bytes));
        }
        return result;
    }
}
