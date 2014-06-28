package com.wedo.businessserver.css3.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 文件夹表
 * 
 * @author c90003207
 * 
 */
@Entity
@Table(name = "ci_folder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WFolder
    implements Serializable
{

    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -808813347322772408L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 版本
     */
    private String version;

    /**
     * guid
     */
    private String guid;

    /**
     * 仓库guid
     */
    private String repguid;

    /**
     * 应用guid
     */
    private String appGuid;

    /**
     * 名称
     */
    private String name;

    /**
     * 父节点guid
     */
    private String paguid;

    /**
     * 创建时间
     */
    private Calendar createTime;

    /**
     * 上传流量
     */
    private BigDecimal totalUpFlux;

    /**
     * 下载流量
     */
    private BigDecimal totalDownFlux;

    /**
     * 下载次数
     */
    private BigDecimal totalDownFileNum;

    /**
     * 占用空间
     */
    private BigDecimal costMemory;

    /**
     * 路径
     */
    private String treepath;

    /**
     * 可用表示（0：可用 1：逻辑删除 2：物理删除）
     */
    private String avaible;

    /**
     * uuid
     */
    private String folderUuid;

    /**
     * tag
     */
    private String tag;

    /**
     * 统计表示（0：是 1：不是）
     */
    private String statistics;

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
     * version的get方法
     * 
     * @return 返回值
     */
    @Column(name = "edition")
    public String getVersion()
    {
        return version;
    }

    /**
     * version的set方法
     * 
     * @param version version
     */
    public void setVersion(String version)
    {
        this.version = version;
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
     * @param appGuid appguid
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
     * paguid的get方法
     * 
     * @return 返回值
     */
    public String getPaguid()
    {
        return paguid;
    }

    /**
     * paguid的set方法
     * 
     * @param paguid paguid
     */
    public void setPaguid(String paguid)
    {
        this.paguid = paguid;
    }

    /**
     * createTime的get方法
     * 
     * @return 返回值
     */
    public Calendar getCreateTime()
    {
        return createTime;
    }

    /**
     * createTime的set方法
     * 
     * @param createTime createTime
     */
    public void setCreateTime(Calendar createTime)
    {
        this.createTime = createTime;
    }

    /**
     * totalUpFlux的get方法
     * 
     * @return 返回值
     */
    public BigDecimal getTotalUpFlux()
    {
        return totalUpFlux;
    }

    /**
     * totalUpFlux的set方法
     * 
     * @param totalUpFlux totalUpFlux
     */
    public void setTotalUpFlux(BigDecimal totalUpFlux)
    {
        this.totalUpFlux = totalUpFlux;
    }

    /**
     * totalDownFlux的get方法
     * 
     * @return 返回值
     */
    public BigDecimal getTotalDownFlux()
    {
        return totalDownFlux;
    }

    /**
     * totalDownFlux的set方法
     * 
     * @param totalDownFlux totalDownFlux
     */
    public void setTotalDownFlux(BigDecimal totalDownFlux)
    {
        this.totalDownFlux = totalDownFlux;
    }

    /**
     * totalDownFileNum的get方法
     * 
     * @return 返回值
     */
    public BigDecimal getTotalDownFileNum()
    {
        return totalDownFileNum;
    }

    /**
     * totalDownFileNum的set方法
     * 
     * @param totalDownFileNum totalDownFileNum
     */
    public void setTotalDownFileNum(BigDecimal totalDownFileNum)
    {
        this.totalDownFileNum = totalDownFileNum;
    }

    /**
     * cost_Memory的get方法
     * 
     * @return 返回值
     */
    @Column(name = "cost_Memory")
    public BigDecimal getCostMemory()
    {
        return costMemory;
    }

    /**
     * costMemory的set方法
     * 
     * @param costMemory costMemory
     */
    public void setCostMemory(BigDecimal costMemory)
    {
        this.costMemory = costMemory;
    }

    /**
     * treepath的get方法
     * 
     * @return 返回值
     */
    public String getTreepath()
    {
        return treepath;
    }

    /**
     * treepath的set方法
     * 
     * @param treepath treepath
     */
    public void setTreepath(String treepath)
    {
        this.treepath = treepath;
    }

    /**
     * avaible的get方法
     * 
     * @return 返回值
     */
    public String getAvaible()
    {
        return avaible;
    }

    /**
     * avaible的set方法
     * 
     * @param avaible avaible
     */
    public void setAvaible(String avaible)
    {
        this.avaible = avaible;
    }

    /**
     * folderuuid的get方法
     * 
     * @return 返回值
     */
    @Column(name = "folderuuid")
    public String getFolderUuid()
    {
        return folderUuid;
    }

    /**
     * folderuuid的set方法
     * 
     * @param folderUuid folderuuid
     */
    public void setFolderUuid(String folderUuid)
    {
        this.folderUuid = folderUuid;
    }

    /**
     * tag的get方法
     * 
     * @return 返回值
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * tag的set方法
     * 
     * @param tag tag
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * statistics的get方法
     * 
     * @return 返回值
     */
    public String getStatistics()
    {
        return statistics;
    }

    /**
     * statistics的set方法
     * 
     * @param statistics statistics
     */
    public void setStatistics(String statistics)
    {
        this.statistics = statistics;
    }

}
