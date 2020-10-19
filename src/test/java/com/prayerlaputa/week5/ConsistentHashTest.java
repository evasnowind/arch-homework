package com.prayerlaputa.week5;

import com.prayerlaputa.week5.consistenthashing.HashAlgorithmEnum;
import com.prayerlaputa.week5.consistenthashing.KetamaConsistentHashLocator;
import com.prayerlaputa.week5.consistenthashing.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenglong.yu
 * created on 2020/10/20
 */
public class ConsistentHashTest {

    public static void main(String[] args) throws InterruptedException {
        //10个节点
        Set<Node> nodeSet = new HashSet<Node>();
        nodeSet.add(new Node("192.168.10.1"));
        nodeSet.add(new Node("192.168.10.2"));
        nodeSet.add(new Node("192.168.10.3"));
        nodeSet.add(new Node("192.168.10.4"));
        nodeSet.add(new Node("192.168.10.5"));
        nodeSet.add(new Node("192.168.10.6"));
        nodeSet.add(new Node("192.168.10.7"));
        nodeSet.add(new Node("192.168.10.8"));
        nodeSet.add(new Node("192.168.10.9"));
        nodeSet.add(new Node("192.168.1.10"));

        //10个节点，每个物理节点对应20个虚拟节点，采用Ketama算法做hash
        KetamaConsistentHashLocator<Node> consistentHashLocator = new KetamaConsistentHashLocator<>(nodeSet, HashAlgorithmEnum.KETAMA_HASH, 20);

        //多线程测试：threadCnt个线程，每个循环访问loopCnt次数
        final int threadCnt = 40, loopCnt = 2000;
        ConsistentHashTest testConsistentHash = new ConsistentHashTest();
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt * loopCnt);

        long start = System.currentTimeMillis();

        for (int i = 0; i < threadCnt; i++) {
            final String name = "thread" + i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int h = 0; h < loopCnt; h++) {
                        Node node = consistentHashLocator.getNodeByKey(name + h);
                        testConsistentHash.statisticHit(node);
                        countDownLatch.countDown();
                    }
//                    testConsistentHash.print();
                }
            }, name);
            t.start();
        }
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));

        countDownLatch.await();
        testConsistentHash.print();

        System.out.println("删除前虚拟节点个数=" + consistentHashLocator.getNodeMap().size());
        Node node = consistentHashLocator.getNodeByKey("abc");
        consistentHashLocator.removeNode(node);
        System.out.println("删除一个节点后虚拟节点个数=" + consistentHashLocator.getNodeMap().size());
    }

    public synchronized void statisticHit(Node node) {
        Long count = stat.get(node.getIp());
        if (count == null) {
            stat.put(node.getIp(), 1L);
        } else {
            stat.put(node.getIp(), count + 1);
        }
    }

    final ConcurrentHashMap<String, Long> stat = new ConcurrentHashMap<String, Long>();

    public void print() {
        long all = 0;
        for (Map.Entry<String, Long> entry : stat.entrySet()) {
            long num = entry.getValue();
            all += num;
            System.out.println("IP:" + entry.getKey() + " hits:" + num);
        }
        System.out.println("all：" + all);
    }
}
