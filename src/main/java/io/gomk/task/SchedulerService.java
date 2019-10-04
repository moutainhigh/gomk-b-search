package io.gomk.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
@Component
@EnableScheduling
public class SchedulerService {

    @Scheduled(fixedRate = 1000)
    public void task1(){
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedRate  {}"+(System.currentTimeMillis()/1000));

    }

//    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
//    @Scheduled(cron = "0/5 * * * * *")
//    public void run() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**fixedRate:上一次开始执行时间点之后5秒再执行*/
//    @Scheduled(fixedRate = 5000)
//    public void run1() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedRate  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
//    @Scheduled(fixedDelay = 5000)
//    public void run2() throws InterruptedException {
//        Thread.sleep(7000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedDelay  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次*/
//    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
//    public void run3(){
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用initialDelay  {}"+(System.currentTimeMillis()/1000));
//    }
}
