package com.wedo.businessserver.event;

import org.springframework.context.ApplicationEvent;

/**
 * 消息对象
 * 
 * @author c90003207
 * 
 */
public class MessageEvent
    extends ApplicationEvent
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * message
     */
    private Object message;
    
    /**
     * constructor
     * 
     * @param source source
     * @param message message
     */
    public MessageEvent(Object source, Object message)
    {
        super(source);
        this.message = message;
    }
    
    /**
     * message
     * 
     * @param message message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * message
     * 
     * @return message
     */
    public Object getMessage()
    {
        return message;
    }
}
