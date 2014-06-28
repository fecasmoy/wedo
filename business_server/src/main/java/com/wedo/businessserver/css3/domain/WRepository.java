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
 * 仓库表
 * 
 * @author c90003207
 * 
 */
@Entity
@Table(name = "ci_repository")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WRepository
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
     * 仓库名称
     */
    private String name;

    /**
     * 共享模式
     */
    private Integer sharestyle;

    /**
     * 仓库uuid
     */
    private String repuuid;

    /**
     * uri
     */
    private String uri;

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

    @Column(name = "appguid")
    public String getAppGuid()
    {
        return appGuid;
    }

    /**
     * appGuid的set方法
     * 
     * @param appGuid appGuid
     */
    public void setAppGuid(String appGuid)
    {
        this.appGuid = appGuid;
    }

    /**
     * name的get方法
     * 
     * @return 返回值
     */
    public String getName()
    {
        return name;
    }

    /**
     * name的set方法
     * 
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * sharestyle的get方法
     * 
     * @return 返回值
     */
    public Integer getSharestyle()
    {
        return sharestyle;
    }

    /**
     * sharestyle的set方法
     * 
     * @param sharestyle sharestyle
     */
    public void setSharestyle(Integer sharestyle)
    {
        this.sharestyle = sharestyle;
    }

    /**
     * repuuid的get方法
     * 
     * @return 返回值
     */
    public String getRepuuid()
    {
        return repuuid;
    }

    /**
     * repuuid的set方法
     * 
     * @param repuuid repuuid
     */
    public void setRepuuid(String repuuid)
    {
        this.repuuid = repuuid;
    }

    /**
     * uri的get方法
     * 
     * @return 返回值
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * uri的set方法
     * 
     * @param uri uri
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }
}
