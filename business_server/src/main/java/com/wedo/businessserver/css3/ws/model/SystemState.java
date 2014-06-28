package com.wedo.businessserver.css3.ws.model;

/**
 * 系统状态 webservice pojo
 * 
 * @author c90003207
 * 
 */
public class SystemState
{
    private String ip;
    
    /** cpu使用率. */
    private Double cpuRatio;
    
    /** 内存使用率. */
    private Double memRatio;
    
    public String getIp()
    {
        return ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public Double getCpuRatio()
    {
        return cpuRatio;
    }
    
    public void setCpuRatio(Double cpuRatio)
    {
        this.cpuRatio = cpuRatio;
    }
    
    public Double getMemRatio()
    {
        return memRatio;
    }
    
    public void setMemRatio(Double memRatio)
    {
        this.memRatio = memRatio;
    }
}
