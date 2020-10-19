# week 5题目1

用你熟悉的编程语言实现一致性 hash 算法。  

实现参见 com.prayerlaputa.week5.consistenthashing 下的实现 KetamaConsistentHashLocator。

## 实现说明

主要算法实现参见KetamaConsistentHashLocator，支持如下功能：  
- 给出所需节点信息，创建一致性哈希表
- 在该哈希表查找
- 删除节点

### 创建一致性哈希环
参见KetamaConsistentHashLocator构造方法。
hash算法采用了Ketama算法。

### 查询
参见KetamaConsistentHashLocator#getNodeByKey方法
基本思想
```text
            if key不在nodeMap中
                按照顺时针方向找下一个元素。
                if 没有下一个元素
                    则返回hash环中第一个元素
            else
                直接返回元素即可
```

### 删除
参见KetamaConsistentHashLocator#removeNode方法

## 测试用例

目前已提交的测试代码参见test/java/com/prayerlaputa/week5/，模拟10个物理节点，每个物理节点对应20个虚拟节点，采用Ketama算法做hash

测试结果如下：

```text
//第一次运行结果
总耗时：7
IP:192.168.10.9 hits:9313
IP:192.168.10.8 hits:10633
IP:192.168.1.10 hits:5697
IP:192.168.10.1 hits:12367
IP:192.168.10.3 hits:6749
IP:192.168.10.2 hits:7739
IP:192.168.10.5 hits:7272
IP:192.168.10.4 hits:5383
IP:192.168.10.7 hits:6809
IP:192.168.10.6 hits:8038
all：80000

//第二次运行结果
总耗时：6
IP:192.168.10.9 hits:9313
IP:192.168.10.8 hits:10633
IP:192.168.1.10 hits:5697
IP:192.168.10.1 hits:12367
IP:192.168.10.3 hits:6749
IP:192.168.10.2 hits:7739
IP:192.168.10.5 hits:7272
IP:192.168.10.4 hits:5383
IP:192.168.10.7 hits:6809
IP:192.168.10.6 hits:8038
all：80000

//第三次运行结果
总耗时：6
IP:192.168.10.9 hits:9313
IP:192.168.10.8 hits:10633
IP:192.168.1.10 hits:5697
IP:192.168.10.1 hits:12367
IP:192.168.10.3 hits:6749
IP:192.168.10.2 hits:7739
IP:192.168.10.5 hits:7272
IP:192.168.10.4 hits:5383
IP:192.168.10.7 hits:6809
IP:192.168.10.6 hits:8038
all：80000
```

## 参考资料
- [一致性Hash算法](https://github.com/ssslinppp/algorithm/tree/master/ketama)
- [【并发编程】使用BlockingQueue实现<多生产者，多消费者>](http://www.cnblogs.com/ssslinppp/p/6279796.html)   
- [五分钟理解一致性哈希算法(consistent hashing)](http://blog.csdn.net/cywosp/article/details/23397179)   
- [一致性哈希算法的理解与实践](https://yikun.github.io/2016/06/09/%E4%B8%80%E8%87%B4%E6%80%A7%E5%93%88%E5%B8%8C%E7%AE%97%E6%B3%95%E7%9A%84%E7%90%86%E8%A7%A3%E4%B8%8E%E5%AE%9E%E8%B7%B5/)    
- [KetamaConsistentHash.java包括动态添加和删除node](https://gist.github.com/linux-china/7817485)
- [https://github.com/RJ/ketama](https://github.com/RJ/ketama)
- [https://github.com/bootsrc/distarch/tree/master/db/consistent-hash](https://github.com/bootsrc/distarch/tree/master/db/consistent-hash)