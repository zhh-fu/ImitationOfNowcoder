package com.zhh_fu.mynowcoder.ThreadKnowledge;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThread {
    //方法一：继承Thread类，重写run方法，并使用start方法启动
    static class MyThread1 extends Thread {
        private int tid;

        public MyThread1(int tid) {
            this.tid = tid;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i < 11; i++) {
                    Thread.sleep(100);
                    System.out.println("当前为第 " + tid + " 个线程打印的第 "
                            + i + " 个数");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    //方法二：实现Runnable接口，重写run方法，并使用start方法启动
    static class MyThread2 implements Runnable {

        @Override
        public void run() {
            try {
                for (int i = 1; i < 4; i++) {
                    Thread.sleep(100);
                    System.out.println("当前为T2打印的第 " + i + " 个数");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void testThread(){
        for (int i=1;i<11;i++){
            new MyThread1(i).start();
        }

        for (int j=1;j<11;j++){
            //注意此处线程的创建方式
            //它实现了Runnable接口，它的启动方式
            new Thread(new MyThread2()).start();
        }
    }

    //要同步的资源
    //也就是钥匙
    private static Object obj = new Object();
    private static void testSynchronized1(){
        synchronized (obj){
            try {
                for (int i = 1; i < 4; i++) {
                    Thread.sleep(100);
                    System.out.println("当前为T1打印的第 " + i + " 个数");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void testSynchronized2(){
        synchronized (new Object()){
            try {
                for (int i = 1; i < 4; i++) {
                    Thread.sleep(100);
                    System.out.println("当前为T2打印的第 " + i + " 个数");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void testSynchronized(){
        for (int i = 0;i< 4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    static class Consumer implements Runnable{
        private BlockingQueue<String> q;
        public Consumer(BlockingQueue<String> q){
            this.q = q;
        }
        @Override
        public void run(){
            try {
                while (true){
                    System.out.println(Thread.currentThread().getName() + " : " + q.take());
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    static class Producer implements Runnable{
        private BlockingQueue<String> q;

        public Producer(BlockingQueue<String> q){
            this.q = q;
        }

        @Override
        public void run(){
            try {
                for (int i=1;i<20;i++){
                    Thread.sleep(100);
                    q.put(String.valueOf(i));
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void  testBlockingQueue(){
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }


    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
    private static int userId;
    public static void testThreadLocal(){
        for (int i=0;i<11;i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        userId = finalI;
                        Thread.sleep(100);
                        System.out.println("userId: " + userId );
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor(){
        //单线程
        //ExecutorService service = Executors.newSingleThreadExecutor();
        //相当于两个线程
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<11;i++) {
                    try {
                        Thread.sleep(100);
                        System.out.println("Executor1: " + i);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<11;i++) {
                    try {
                        Thread.sleep(100);
                        System.out.println("Executor2: " + i);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private static int count = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAtomic(){
        for (int i=0;i<10;++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(500);
                        for (int j=0;j<10;++j){
                            //count++;
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception{
                Thread.sleep(1000);
                throw new IllegalArgumentException("异常");
                //return 1;
            }
        });

        try {
            System.out.println(future.get());
            //System.out.println(future.get(100,TimeUnit.MILLISECONDS));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testWithoutAtomic();
        testFuture();
    }
}
