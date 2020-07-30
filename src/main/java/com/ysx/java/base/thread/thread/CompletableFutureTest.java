package com.ysx.java.base.thread.thread;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description: CompletableFuture 是对 Future 扩展
 *  参考 https://www.jianshu.com/p/6bac52527ca4
 *       https://colobu.com/2016/02/29/Java-CompletableFuture/
 * @Author: ysx
 * @Date: 2020/5/25 17:15
 */
public class CompletableFutureTest {



    public static void main(String[] args) throws Exception {
       // runAsync();
       // supplyAsync();
       // whenComplete();
      //  whenCompleteAsync();
       // whenCompleteWithResult();
      //  thenApply();
       // handle();
      //  thenAccept();
      //  thenRun();
        thenCombine();
    }


    /**
     *  Future 是Java 5添加的类，用来描述一个异步计算的结果。你可以使用isDone方法检查计算是否完成，或者使用get阻塞住调用线程，直到计算完成返回结果，你也可以使用cancel方法停止任务的执行。
     *  虽然Future以及相关使用方法提供了异步执行任务的能力，但是对于结果的获取却是很不方便，只能通过阻塞或者轮询的方式得到任务的结果
     *
     */
    private static void testFuture() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<Integer> f = es.submit(() ->{
            // 长时间的异步计算
            // ……
            // 然后返回结果
            return 100;
        });
//        while(!f.isDone())
//            ;
        f.get();
    }

    /**
     *  简单测试 - 无返回值
     */
    private static void runAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                System.out.println("future run start ...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future run end ...");
        });
        System.out.println("runAsync method run end ...");
        future.get();
    }

    /**
     * 简单测试 - 有返回值
     */
    private static void supplyAsync() throws Exception {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("future run start ...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future run end ...");
            return System.currentTimeMillis();
        });
        System.out.println("supplyAsync method run end ... ");
        long time = future.get();
        System.out.println("supplyAsync method run end ... time = "+time);
    }


    /**
     *  测试 whenComplete
     *  whenComplete 和 whenCompleteAsync 的区别：
     * whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     * whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行 -- 如果使用相同的线程池，也可能会被同一个线程选中执行。
     */
    public static void whenComplete() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                System.out.println("future run start ..."+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if(new Random().nextInt()%2>=0) {
                int i = 12/0;
            }
            System.out.println("future run end ..."+Thread.currentThread().getName());
        });

        future.whenComplete(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void t, Throwable action) {
                System.out.println("future 执行完成！"+Thread.currentThread().getName());
            }
        });
        future.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable t) {
                System.out.println("future 执行失败！"+t.getMessage());
                return null;
            }
        });
        System.out.println("whenComplete method end1"+Thread.currentThread().getName());
        // 这里不睡一下，主线程直接结束
       TimeUnit.SECONDS.sleep(2);
        System.out.println("whenComplete method end2");

    }


    /**
     *  和 thenApply 的区别是
     *    whenComplete 并不能改变 future 结果，他只是拿到第一个 CompletableFuture 的结果，做一些业务处理，返回一个新的 CompletableFuture，结果仍是第一个CompletableFuture结果
     *    thenApply 拿到结果 修改后 返回 CompletableFuture ，此时 CompletableFuture 得到的是修改后的结果
     */
    private static void whenCompleteWithResult() throws Exception {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("future run start ..."+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future run end ..."+Thread.currentThread().getName());
            return 10L;
        });

        CompletableFuture<Long> complete = future.whenComplete(new BiConsumer<Long, Throwable>() {
            @Override
            public void accept(Long aLong, Throwable throwable) {

                System.out.println(aLong+"future 执行完成！" + Thread.currentThread().getName());
                aLong = 20L;
            }

        });
        System.out.println("结果是:"+ complete.get());

        System.out.println("whenComplete method end1"+Thread.currentThread().getName());
        // 这里不睡一下，主线程直接结束
        TimeUnit.SECONDS.sleep(2);
        System.out.println("whenComplete method end2");

    }


    /**
     *  测试 whenComplete
     *  whenComplete 和 whenCompleteAsync 的区别：
     * whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     * whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行 -- 如果使用相同的线程池，也可能会被同一个线程选中执行。
     */
    public static void whenCompleteAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                System.out.println("future run start ..."+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if(new Random().nextInt()%2>=0) {
                int i = 12/0;
            }
            System.out.println("future run end ..."+Thread.currentThread().getName());
        });

        future.whenCompleteAsync(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void t, Throwable action) {
                System.out.println("future 执行完成！"+Thread.currentThread().getName());
            }
        });
        future.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable t) {
                System.out.println("future 执行失败！"+t.getMessage());
                return null;
            }
        });
        System.out.println("whenComplete method end1"+Thread.currentThread().getName());
        // 这里不睡一下，主线程直接结束
        TimeUnit.SECONDS.sleep(2);
        System.out.println("whenComplete method end2");
    }


    /**
     *  当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化。
     *   抛出异常 不执行 thenApply
     *
     *     *  和 thenApply 的区别是
     *      *    whenComplete 并不能改变 future 结果，他只是拿到第一个 CompletableFuture 的结果，做一些业务处理，返回一个新的 CompletableFuture，结果仍是第一个CompletableFuture结果
     *      *    thenApply 拿到结果 修改后 返回 CompletableFuture ，此时 CompletableFuture 得到的是修改后的结果
     *
     */

    private static void thenApply() throws Exception {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(new Supplier<Long>() {
            @Override
            public Long get() {
                long result = new Random().nextInt(100);
                System.out.println("result1="+result);
                return result;
            }
        }).thenApply(new Function<Long, Long>() {
            @Override
            public Long apply(Long t) {
                long result = t*5;
                System.out.println("result2="+result);
                return result;
            }
        });
        long result = future.get();
        System.out.println(result);
    }

    /**
     *  相比于 thenApply 方法 ，handle 方法可以处理异常
     */
    public static void handle() throws Exception{
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int i= 10/0;
                return new Random().nextInt(10);
            }
        }).handle(new BiFunction<Integer, Throwable, Integer>() {
            @Override
            public Integer apply(Integer param, Throwable throwable) {
                int result = -1;
                if(throwable==null){
                    result = param * 2;
                }else{
                    System.out.println(throwable.getMessage());
                }
                return result;
            }
        });
        System.out.println(future.get());
    }


    /**
     * whenComplete  接收任务的处理结果，并返回接收到的结果
     * thenAccept 接收任务的处理结果，并消费处理，无返回结果。
     * thenApply 接收任务的处理结果，并返回处理后的结果。
     * thenRun 不接收任务处理结果，无返回结果
     */
    private static void thenAccept() throws Exception{
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(10);
            }
        }).thenAccept(integer -> {
            System.out.println(integer);
        });

    }

    private static void thenRun() throws Exception{
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(10);
            }
        }).thenRun(() -> {
            System.out.println("thenRun ...");
        });
        System.out.println(future.get());
    }


    /**
     * thenCombine 会把 两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理。
     * 返回 一个处理后的结果
     */
    private static void thenCombine() throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello1";
            }
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello2";
            }
        });
        CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String t, String u) {
                return t+" "+u;
            }
        });
        System.out.println(result.get());
    }

    /**
     * thenAcceptBoth 会把 两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理。
     * 和 thenCombine 的区别是 thenAcceptBoth 不会返回结果
     */
    private static void thenAcceptBoth() throws Exception {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.thenAcceptBoth(f2, new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer t, Integer u) {
                System.out.println("f1="+t+";f2="+u+";");
            }
        });
    }


    /**
     * 两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）
     */
    private static void runAfterEither() throws Exception {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.runAfterEither(f2, new Runnable() {

            @Override
            public void run() {
                System.out.println("上面有一个已经完成了。");
            }
        });
    }

    /**
     * 两个CompletionStage，都完成了计算才会执行下一步的操作（Runnable）
     */
    private static void runAfterBoth() throws Exception {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.runAfterBoth(f2, new Runnable() {

            @Override
            public void run() {
                System.out.println("上面两个任务都执行完成了。");
            }
        });
    }

    /**
     * allOf方法是当所有的CompletableFuture都执行完后执行计算。
     *
     * anyOf方法是当任意一个CompletableFuture执行完后就会执行计算，计算的结果相同
     */
    private static void other() throws ExecutionException, InterruptedException {
        Random rand = new Random();
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });
        //CompletableFuture<Void> f =  CompletableFuture.allOf(future1,future2);
        CompletableFuture<Object> f =  CompletableFuture.anyOf(future1,future2);
        System.out.println(f.get());
    }

}
