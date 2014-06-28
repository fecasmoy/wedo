package com.wedo.businessserver.css3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 应用仓库中间表
 * 
 * @author c90003207
 * 
 */
@Entity
@Table(name = "ci_repository_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WRepApp
{
    /**
     * 主键
     */
    private Long id;
    
    /**
     * guid
     */
    private String guid;
    
    /**
     * 应用guid
     */
    private String appGuid;
    
    /**
     * 仓库guid
     */
    private String repguid;
    
    /**
     * 权限
     */
    private String jurisdiction;
    
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
     * appguid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "appguid")
    public String getAppGuid()
    {
        return appGuid;
    }
    
    /**
     * appguid的set方法
     * 
     * @param appguid appguid
     */
    public void setAppGuid(String appguid)
    {
        this.appGuid = appguid;
    }
    
    /**
     * repguid的get方法
     * 
     * @return 返回值
     */
    public String getRepguid()
    {
        return repguid;
    }
    
    /**
     * repguid的set方法
     * 
     * @param repguid repguid
     */
    public void setRepguid(String repguid)
    {
        this.repguid = repguid;
    }
    
    /**
     * jurisdiction的get方法
     * 
     * @return 返回值
     */
    public String getJurisdiction()
    {
        return jurisdiction;
    }
    
    /**
     * jurisdiction的set方法
     * 
     * @param jurisdiction jurisdiction
     */
    public void setJurisdiction(String jurisdiction)
    {
        this.jurisdiction = jurisdiction;
    }
}
