package com.wedo.businessserver.attemper.threadpool;

import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 线程执行类
 * 
 * @author c90003207
 * 
 */
public class WorkThread
    extends Thread
{

    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory
        .getLog(WorkThread.class);

    // sleep times
    private static final int THREAD_SLEEP_TIME_1000 = 1000;

    // 是否停止线程标志
    private boolean shutdown = false;

    // 队列任务
    private Queue<Task> queue;

    /**
     * 初始化工作线程
     * 
     * @param queue queue
     */
    public WorkThread(Queue<Task> queue)
    {
        this.queue = queue;
    }

    /**
     * run method
     */
    public void run()
    {
        while (!shutdown)
        {
            try
            {
                // 休眠一秒
                Thread
                    .sleep(THREAD_SLEEP_TIME_1000);
            }
            catch (InterruptedException e1)
            {
                logger
                    .error("error", e1);
                e1
                    .printStackTrace();
            }
            task();
        }
    }

    /**
     * TASK METHOD
     */
    public void task()
    {
        synchronized (queue)
        {
            if (!queue
                .isEmpty())
            {
                // 顺序取任务
                Task task = queue
                    .poll();
                // 任务执行
                task
                    .execute();
            }
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown()
    {
        shutdown = true;
    }
}
