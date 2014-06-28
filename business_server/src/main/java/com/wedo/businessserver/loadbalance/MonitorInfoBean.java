package com.wedo.businessserver.loadbalance;

import java.io.Serializable;

/**
 * cpu，内存监控pojs
 * 
 * @author c90003207
 * 
 */
public class MonitorInfoBean
    implements Serializable
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -424402026575648725L;
    
    /** 本机IP */
    private String localIp;
    
    /** cpu可使用率. */
    private Double cpuRatio = Double.valueOf(0);
    
    /** 内存可使用率. */
    private Double memRatio = Double.valueOf(0);
    
    /**
     * cpuRatio
     * 
     * @return cpuRatio
     */
    public Double getCpuRatio()
    {
        return cpuRatio;
    }
    
    /**
     * cpuRatio
     * 
     * @param cpuRatio cpuRatio
     */
    public void setCpuRatio(Double cpuRatio)
    {
        this.cpuRatio = cpuRatio;
    }
    
    /**
     * memRatio
     * 
     * @return memRatio
     */
    public Double getMemRatio()
    {
        return memRatio;
    }
    
    /**
     * memRatio
     * 
     * @param memRatio memRatio
     */
    public void setMemRatio(Double memRatio)
    {
        this.memRatio = memRatio;
    }
    
    /**
     * localIp
     * 
     * @return localIp
     */
    public String getLocalIp()
    {
        return localIp;
    }
    
    /**
     * localip
     * 
     * @param localIp localIp
     */
    public void setLocalIp(String localIp)
    {
        this.localIp = localIp;
    }
    
    /**
     * toString
     * 
     * @return {@link String}
     */
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return "localIp=" + localIp + ";cpuRatio=" + cpuRatio + ";memRatio="
            + memRatio;
    }
    
}
