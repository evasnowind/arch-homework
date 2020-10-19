package com.prayerlaputa.week5.consistenthashing;

import java.util.Collection;
import java.util.TreeMap;

/**
 * @author chenglong.yu
 * created on 2020/10/20
 */
public class KetamaConsistentHashLocator<T> {


    /**
     * 默认虚拟节点数量
     */
    private int virtualNum = 10;
    private static final int DEFAULT_VIRTUAL_NODE_NUM = 10;

    /**
     * 一致性hash常用ketama算法做hash
     * 对于一致性HASH而言选择的HASH算法首先要考虑发散度其次再考虑性能
     */
    private HashAlgorithmEnum hashAlgo = HashAlgorithmEnum.KETAMA_HASH;

    private final TreeMap<Long/*节点hash*/, T/*节点*/> nodeMap = new TreeMap<>();


    public KetamaConsistentHashLocator(Collection<T> nodes) {
        this(nodes, HashAlgorithmEnum.KETAMA_HASH);
    }

    public KetamaConsistentHashLocator(Collection<T> nodes, HashAlgorithmEnum hashAlgo) {
        this(nodes, hashAlgo, DEFAULT_VIRTUAL_NODE_NUM);
    }

    public KetamaConsistentHashLocator(Collection<T> nodes, HashAlgorithmEnum hashAlgo, int virtualNum) {
        this.hashAlgo = hashAlgo;
        this.virtualNum = virtualNum;

        for (T node : nodes) {
            for (int i = 0; i < virtualNum; i++) {
                long nodeKey = this.hashAlgo.hash(node.toString() + i);
                nodeMap.put(nodeKey, node);
            }
        }
    }

    public T getNodeByKey(final String key) {
        if (nodeMap.isEmpty()) {
            return null;
        }
        final Long hash = this.hashAlgo.hash(key);
        Long target = hash;
        if (!nodeMap.containsKey(target)) {
            /*
            if key不在nodeMap中
                按照顺时针方向找下一个元素。
                if 没有下一个元素
                    则返回hash环中第一个元素
            else
                直接返回元素即可


            此处通过使用TreeMap，将顺时针查找转换成查找下一个更大元素的操作。
            注意：ceilingKey方法返回大于或等于给定键元素(ele)的下键元素，否则返回null。
             */
            target = this.nodeMap.ceilingKey(target);
            if (null == target && !nodeMap.isEmpty()) {
                target = nodeMap.firstKey();
            }
        }

        return nodeMap.get(target);
    }

    public TreeMap<Long, T> getNodeMap() {
        return nodeMap;
    }

    /**
     * 注意，我们在一致性hash查找时，返回的节点是物理节点信息。
     * 当我们需要删除时，需要根据物理节点、删除该节点对应的所有虚拟节点信息。
     * 为此，删除节点的方法参数需要传入物理节点信息，以便根据
     * 一致性hash内部生成虚拟节点的算法，找出该节点对应的所有虚拟节点并删除。
     * @param node
     */
    public void removeNode(final T node) {
        if (nodeMap.isEmpty()) {
            return;
        }

        for (int i = 0; i < virtualNum; i++) {
            long nodeKey = this.hashAlgo.hash(node.toString() + i);
            nodeMap.remove(nodeKey, node);
        }
    }
}
