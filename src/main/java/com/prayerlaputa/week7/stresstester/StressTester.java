package com.prayerlaputa.week7.stresstester;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenglong.yu
 * created on 2020/11/3
 */
public class StressTester {

    private String url = "http://www.baidu.com";

    private long totalRequestNum;
    private int concurrentNum;

    private ExecutorService executorService;
    private CloseableHttpAsyncClient httpclient;

    private CountDownLatch latch;

    /**
     * 多线程put的情况下，HashMap可能出现死锁，因此必须使用ConcurrentHashMap
     */
    private ConcurrentHashMap<Long, Long> respCostMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, Long> reqTimeMap = new ConcurrentHashMap<>();

    public StressTester(final String url, final long totalRequestNum, final int concurrentNum) {
        this.concurrentNum = concurrentNum;
        this.totalRequestNum = totalRequestNum;
        this.url = url;

        latch = new CountDownLatch((int)totalRequestNum);

        initThreadPool();
    }

    private void initThreadPool() {
        long keepAliveTime = 1000L;

        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        executorService = new ThreadPoolExecutor(concurrentNum, concurrentNum,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("StressTester"),
                handler);

        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(concurrentNum)
                .setRcvBufSize(32 * 1024)
                .build();

        /*
        使用httpclient异步发送
        maxConnTotal是同时间正在使用的最多的连接数
        maxConnPerRoute是针对一个域名同时间正在使用的最多的连接数
         */

        httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();
        httpclient.start();
    }

    public void startTest() {
        long startTime = System.currentTimeMillis();
        System.out.println("开始测试....");
        for (int i = 0; i < totalRequestNum; i++) {
            final long curId = i;
            executorService.submit(() -> requestGet(curId, url));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("线程被意外终止！");
        }
        long totalCostTime = System.currentTimeMillis() - startTime;
        System.out.println("测试结束，总共用时：" + totalCostTime + " ms");
    }

    /**
     * 关闭http client，否则整个程序无法正常结束。
     */
    public void shutdown() {
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void run() {
        startTest();
        printRespCostSummary();
    }

    private void printRespCostSummary() {
        ArrayList<Long> respCostTimeList = new ArrayList<>(respCostMap.values());
        respCostTimeList.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return (int)(o1 - o2);
            }
        });

        long costTimeFor95 = respCostTimeList.get((int)(totalRequestNum % 0.95));

        long totalCostTime = 0L;
        for (Long cost : respCostTimeList) {
            totalCostTime += cost;
        }
        double averageCostTime = totalCostTime * 1.0 / totalRequestNum;

        System.out.println("请求成功数量：" + respCostMap.size());
        System.out.println("平均响应时间: " + averageCostTime + " ms");
        System.out.println("95分位值：" + costTimeFor95 + " ms");
    }

    private void requestGet(final Long requestId, final String url) {
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);

        reqTimeMap.put(requestId, System.currentTimeMillis());

        httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                processResponse(requestId, httpResponse);
            }

            @Override
            public void failed(Exception e) {
                httpGet.abort();
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }

    private void processResponse(final Long requestId, final HttpResponse response) {
        Long cost = System.currentTimeMillis() - reqTimeMap.get(requestId);
        respCostMap.put(requestId, cost);
//        System.out.println("response for id=" + requestId);
        latch.countDown();
    }


    public static void main(String[] args) {
        String url = "http://www.baidu.com";
        int totalRequestNum = 100;
        int concurrentNum = 10;
        if (args.length >= 3) {
            url = args[0];
            totalRequestNum = Integer.parseInt(args[1]);
            concurrentNum = Integer.parseInt(args[2]);
        } else {
            System.out.println("启动时必须给出URL、总请求数、并发数作为参数！由于缺少参数，程序直接退出！");
        }

        System.out.println("压测参数：");
        System.out.println("URL: " + url);
        System.out.println("总请求数量: " + totalRequestNum);
        System.out.println("并发请求数: " + concurrentNum);
        System.out.println();
        StressTester stressTester = new StressTester(url, totalRequestNum, concurrentNum);
        stressTester.run();
        stressTester.shutdown();
    }
}
