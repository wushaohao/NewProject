package com.hao.distributed.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * zookeeper实现分布式锁
 */
public class DistributedZKLock implements Watcher {
    private static final String LOCK_ROOT = "/locks";
    private static final String LOCK_NODE = LOCK_ROOT + "/lock_";


    private ZooKeeper zk;
    private String currentLockNode;

    public DistributedZKLock(String zkHost) throws IOException {
        this.zk = new ZooKeeper(zkHost, 3000, this);
        // 创建根结点
        try {
            if (zk.exists(LOCK_ROOT, false) == null) {
                zk.create(LOCK_ROOT, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 获取锁
    public void lock() throws KeeperException, InterruptedException {
        currentLockNode = zk.create(LOCK_NODE, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        // 检查是否可以获取锁
        List<String> children = zk.getChildren(LOCK_ROOT, false);
        Collections.sort(children);
        if (currentLockNode.equals(LOCK_ROOT + "/" + children.get(0))) {
            return; //成功获取锁
        } else {
            String predecessor = children.get(Collections.binarySearch(children, currentLockNode.substring(LOCK_ROOT.length() + 1)) - 1);
            zk.exists(LOCK_ROOT + "/" + predecessor, new LockWatcher());
            // Block until the previous node is deleted
            synchronized (this) {
                wait();
            }
        }
    }

    // 释放锁
    public void unlock() throws KeeperException, InterruptedException {
        if (currentLockNode != null) {
            // 检查节点是否存在并获取其版本
            Stat stat = zk.exists(currentLockNode, false);
            if (stat != null) {
                // 获取节点版本
                int version = stat.getVersion();
                zk.delete(currentLockNode, version);
                currentLockNode = null;
            } else {
                System.out.println("Node does not exist " + currentLockNode);
            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify(); //释放锁通知
            }
        }

    }

    public class LockWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                synchronized (DistributedZKLock.this) {
                    DistributedZKLock.this.notify();// 通知等待线程
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            DistributedZKLock lock = new DistributedZKLock("localhost:2181");

            for (int i = 0; i < 5; i++) {
                final int requestId = i;
                new Thread(() -> {
                    try {
                        lock.lock();
                        System.out.println("Thread " + requestId + "acquire the lock.");
                        //模拟处理
                        Thread.sleep(1000);
                        System.out.println("Thread " + requestId + " releasing the lock.");
                        lock.unlock();

                    } catch (KeeperException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
