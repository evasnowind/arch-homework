package com.prayerlaputa.week8.find_first_common_node;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenglong.yu
 * created on 2020/11/11
 */
public class Solution {


    /**
     * leetcode 160 https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
     *
     * @param headA
     * @param headB
     * @return
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (null == headA || null == headB) {
            return null;
        }

        if (!hasCommonNode(headA, headB)) {
            return null;
        }
        /*

         */
        ListNode node1 = headA, node2 = headB;
        while (node1 != node2) {
            node1 = null != node1 ? node1.next : headB;
            node2 = null != node2 ? node2.next : headA;
        }

        return node1;
    }

    private boolean hasCommonNode(ListNode headA, ListNode headB) {
        Set<ListNode> nodeSet = new HashSet<>();
        ListNode node = headA;
        while (null != node) {
            nodeSet.add(node);
            node = node.next;
        }
        node = headB;
        while(null != node) {
            if (nodeSet.contains(node)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }
}

class ListNode {
    int val;
    ListNode next;

    public ListNode(int x) {
        this.val = x;
    }
}
