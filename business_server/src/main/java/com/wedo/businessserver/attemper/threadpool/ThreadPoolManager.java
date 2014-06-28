package com.wedo.businessserver.attemper.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;

/**
 * 线程池管理类
 * 
 * @author c90003207
 * 
 */
public class ThreadPoolManager
{
    
    // 记录日志的时候用到的日志工具类
    private static final Log logger =
        LogFactory.getLog(ThreadPoolManager.class);
    
    // 默认线程池大小
    private static final int DEFAULT_POOL_SIZE = 10;
    
    // Thread Pool Manager
    private static ThreadPoolManager threadPoolManager;
    
    // 本地化处理
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    // 线程池
    private List<WorkThread> threadPool;
    
    // 任务
    private Queue<Task> taskQueue;
    
    // 线程池大小
    private int poolSize;
    
    /**
     * 构造器
     * 
     * @param poolSize size
     */
    private ThreadPoolManager(int poolSize)
    {
        if (poolSize <= 0)
        {
            // 默认线程池
            this.poolSize = DEFAULT_POOL_SIZE;
        }
        else
        {
            this.poolSize = poolSize;
        }

        // 初始化线程池
        threadPool = new ArrayList<WorkThread>(this.poolSize);
        // 线程安全的实现
        taskQueue = new ConcurrentLinkedQueue<Task>();
        startup();
    }
    
    /**
     * 初始化
     * 
     * @return ThreadPoolManager
     */
    public static synchronized ThreadPoolManager getInstance()
    {
        if (threadPoolManager == null)
        {
            threadPoolManager = new ThreadPoolManager(DEFAULT_POOL_SIZE);
        }
        return threadPoolManager;
    }
    
    /**
     * 启动线程池
     */
    public void startup()
    {
        logger.debug(msg.getString("msg.search.start")); // 开始启动搜索线程管理类
        for (int i = 0; i < this.poolSize; i++)
        {
            WorkThread workThread = new WorkThread(taskQueue);
            threadPool.add(workThread); // 添加线程
            workThread.start(); // 启动线程
        }
    }
    
    /**
     * 关闭线程池
     */
    public void shutdown()
    {
        logger.debug(msg.getString("msg.search.close"));
        for (int i = 0; i < this.poolSize; i++)
        {
            threadPool.get(i).shutdown();
        }
    }
    
    /**
     * 添加计划
     * 
     * @param task TASK
     */
    public void addTask(Task task)
    {
        taskQueue.add(task);
    }
}
