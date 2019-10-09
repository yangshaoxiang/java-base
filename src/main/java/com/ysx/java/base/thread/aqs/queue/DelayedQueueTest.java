package com.ysx.java.base.thread.aqs.queue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *  延迟队列  -- 使用优先级队列实现的一个无界队列(会自动扩容) 只有一个 Condition 条件队列(availableCondition 因为无界，永远不会fullCondition)
 *  入队时设置元素可出队的时间，在出队时间到达前，获取元素会阻塞当前获取线程
 *  1. 队列中的元素必须继承 Delayed 接口 即 必须实现 getDelay 方法(获取当前应该延迟的时间，<=0表示已经不需要在延迟) 和 compareTo(return this - args) 方法
 *  2. 延迟队列 是元素在插入队列时，按照 compareTo 接口做排序，若本次入队元素排序后处于队首，则唤醒条件队列的首部节点
 *          当主线程处于 take 阻塞状态时，队列进入一个延迟极低的元素(元素排序后处于队首) 会唤醒 阻塞线程，重新计算阻塞时间或直接消费
 *  3. 元素在 take 时具体需要多久，是元素对象重写的 getDelay() 方法决定，一般就是 过期时间戳 - 当前毫秒值，因此当元素创建出来时，其实已经在计时了
 */
public class DelayedQueueTest {

    public static void main(String[] args) {
        DelayQueue<MovieTiket> delayQueue = new DelayQueue<MovieTiket>();
        MovieTiket tiket = new MovieTiket("电影票0",1000000);
        delayQueue.put(tiket);
        MovieTiket tiket1 = new MovieTiket("电影票1",500000);
        delayQueue.put(tiket1);
        MovieTiket tiket2 = new MovieTiket("电影票2",800000);
        delayQueue.put(tiket2);
        System.out.println("message:--->入队完毕");

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 测试 当主线程处于 take 阻塞状态时，队列进入一个延迟极低的元素是否唤醒 阻塞线程 --- 结论: 当新入队元素排序后处于队首，则唤醒阻塞线程，重新计算阻塞时间
        new Thread(() -> {
            MovieTiket tiket3 = new MovieTiket("子线程电影票2",10);
            delayQueue.put(tiket3); // 断点
        }).start();

        while( delayQueue.size() > 0 ){
            try {
                System.out.println(currentTime()+"电影票预出队");
                tiket = delayQueue.take();
                System.out.println(currentTime()+"电影票出队:"+tiket.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  获取当前时间字符串
     */
    private static String currentTime(){
        return "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-";
    }

}

class MovieTiket implements Delayed {
    //延迟时间
    private final long delay;
    //到期时间
    private final long expire;
    //数据
    private final String msg;
    //创建时间
    private final long now;

    public long getDelay() {
        return delay;
    }

    public long getExpire() {
        return expire;
    }

    public String getMsg() {
        return msg;
    }

    public long getNow() {
        return now;
    }

    /**
     * @param msg 消息
     * @param delay 延期时间
     */
    public MovieTiket(String msg , long delay) {
        this.delay = delay;
        this.msg = msg;
        expire = System.currentTimeMillis() + delay;    //到期时间 = 当前时间+延迟时间
        now = System.currentTimeMillis();
    }

    /**
     * @param msg
     */
    public MovieTiket(String msg){
        this(msg,1000);
    }

    public MovieTiket(){
        this(null,1000);
    }

    /**
     * 获得延迟时间   用应当过期时间-当前时间,时间单位毫秒
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序  当前时间的延迟时间 - 比较对象的延迟时间
     * 越早过期的时间在队列中越靠前
     * @param delayed
     * @return
     */
    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS)
                - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MovieTiket{" +
                "delay=" + delay +
                ", expire=" + expire +
                ", msg='" + msg + '\'' +
                ", now=" + now +
                '}';
    }
}
