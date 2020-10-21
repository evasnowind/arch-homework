package com.prayerlaputa.week5.consistenthashing;

import com.sun.xml.internal.ws.api.Component;

/**
 * 节点的IP实现
 */
public class Node implements Comparable {

    private String name;

    private String ip;

    public Node(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public Node(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {

        if (name != null && !"".equals(name)) {
            return ip + "-" + name;
        }
        return ip;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Node node = (Node) o;
        if (node.getIp() == null && ip == null && node.getName() == null && name == null) {
            return true;
        }
        if (name == null && node.getName() != null) {
            return false;
        }
        if (ip == null && node.getIp() != null) {
            return false;
        }
        assert ip != null;
        assert name != null;
        return name.equals(node.getName()) && ip.equals(node.getIp());
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return ip.compareTo(((Node)o).getIp());
    }
}
