package com.wedo.businessserver.css3.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 支撑节点
 * 
 * changed log: 20091205 by liuguoxia:增加节点当前写入路径的软链接符号_symName.
 * 
 * @author c90003107
 */
@Entity
@Table(name = "ci_csp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WSupportNode
    implements Serializable
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = 2352385595893833230L;
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 标识（不是唯一标识）
     */
    private String guid;
    
    /**
     * 节点Ip
     */
    private String localIp;
    
    /**
     * 挂载路径
     */
    private String mountPath;
    
    /**
     * 挂载记录唯一标识（非唯一索引）
     */
    private String mountguid;
    
    /**
     * 挂载时间
     */
    private Calendar mountTime;
    
    /**
     * 启用标识（0，1）
     */
    private char invocation;
    
    /**
     * 当前写入挂载路径
     */
    private String hangsCarriesPath;
    
    /**
     * 存储结点guid
     */
    private String csnguid;
    
    /**
     * 挂载路径链接名
     */
    private String symName;
    
    /**
     * id的get方法
     * 
     * @return 返回值
     */
    @Id
    @GeneratedValue(generator = "identity")
    @GenericGenerator(name = "identity", strategy = "identity")
    public Long getId()
    {
        return id;
    }
    
    /**
     * id的set方法
     * 
     * @param id id
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * guid的get方法
     * 
     * @return 返回值
     */
    public String getGuid()
    {
        return guid;
    }
    
    /**
     * guid的set方法
     * 
     * @param guid guid
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    
    /**
     * localIp的get方法
     * 
     * @return 返回值
     */
    public String getLocalIp()
    {
        return localIp;
    }
    
    /**
     * localIp的set方法
     * 
     * @param localIp localIp
     */
    public void setLocalIp(String localIp)
    {
        this.localIp = localIp;
    }
    
    /**
     * mountPath的get方法
     * 
     * @return 返回值
     */
    public String getMountPath()
    {
        return mountPath;
    }
    
    /**
     * mountPath的set方法
     * 
     * @param mountPath mountPath
     */
    public void setMountPath(String mountPath)
    {
        this.mountPath = mountPath;
    }
    
    /**
     * mountguid的get方法
     * 
     * @return 返回值
     */
    public String getMountguid()
    {
        return mountguid;
    }
    
    /**
     * mountguid的set方法
     * 
     * @param mountguid mountguid
     */
    public void setMountguid(String mountguid)
    {
        this.mountguid = mountguid;
    }
    
    /**
     * mountTime的get方法
     * 
     * @return 返回值
     */
    public Calendar getMountTime()
    {
        return mountTime;
    }
    
    /**
     * mountTime的set方法
     * 
     * @param mountTime mountTime
     */
    public void setMountTime(Calendar mountTime)
    {
        this.mountTime = mountTime;
    }
    
    /**
     * invocation的get方法
     * 
     * @return 返回值
     */
    public char getInvocation()
    {
        return invocation;
    }
    
    /**
     * invocation的set方法
     * 
     * @param invocation invocation
     */
    public void setInvocation(char invocation)
    {
        this.invocation = invocation;
    }
    
    /**
     * hangsCarriesPath的get方法
     * 
     * @return 返回值
     */
    public String getHangsCarriesPath()
    {
        return hangsCarriesPath;
    }
    
    /**
     * hangsCarriesPath的set方法
     * 
     * @param hangsCarriesPath hangsCarriesPath
     */
    public void setHangsCarriesPath(String hangsCarriesPath)
    {
        this.hangsCarriesPath = hangsCarriesPath;
    }
    
    /**
     * csnguid的get方法
     * 
     * @return 返回值
     */
    public String getCsnguid()
    {
        return csnguid;
    }
    
    /**
     * csnguid的set方法
     * 
     * @param csnguid csnguid
     */
    public void setCsnguid(String csnguid)
    {
        this.csnguid = csnguid;
    }
    
    /**
     * symName的get方法
     * 
     * @return 返回值
     */
    public String getSymName()
    {
        return symName;
    }
    
    /**
     * symName的set方法
     * 
     * @param symName symName
     */
    public void setSymName(String symName)
    {
        this.symName = symName;
    }
}
