package com.wedo.businessserver.css3.ws.model;

/**
 * 返回状态 webservice pojo
 * 
 * @author c90003207
 * 
 */
public class Result
{
    private Boolean result;
    
    private String message;
    
    public void setResult(Boolean result)
    {
        this.result = result;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public Boolean getResult()
    {
        return this.result;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
}
