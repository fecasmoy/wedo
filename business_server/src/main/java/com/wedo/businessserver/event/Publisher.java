package com.wedo.businessserver.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 消息发布者
 * 
 * @author c90003207
 * 
 */
public class Publisher
    implements ApplicationContextAware
{
    /**
     * context
     */
    private ApplicationContext context;
    
    /**
     * setter
     * 
     * @param arg0 arg
     * @throws BeansException bean exception
     */
    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException
    {
        this.context = arg0;
    }
    
    /**
     * publish method
     * 
     * @param message message
     */
    public void publish(Object message)
    {
        context.publishEvent(new MessageEvent(this, message));
    }
}
