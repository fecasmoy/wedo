package com.wedo.businessserver.css3.ws.model;

/**
 * 增值服务配置文件pojo
 * 
 * @author c90003207
 * 
 */
public class WappProperties
{
    
    /** 应用id */
    private String appId;
    
    /** licence */
    private String licence;
    
    /** 增值服务集合 */
    private Function function = new Function();
    
    /**
     * getter
     * 
     * @return return value
     */
    public Function getFunction()
    {
        return function;
    }
    
    /**
     * setter
     * 
     * @param function function
     */
    public void setFunction(Function function)
    {
        this.function = function;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public String getAppId()
    {
        return appId;
    }
    
    /**
     * setter
     * 
     * @param appId appId
     */
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
    
    /**
     * getter
     * 
     * @return return value
     */
    public String getLicence()
    {
        return licence;
    }
    
    /**
     * getter
     * 
     * @param licence licence
     */
    public void setLicence(String licence)
    {
        this.licence = licence;
    }
    
    /**
     * 方法
     * 
     * @author c90003207
     */
    public class Function
    {
        private SearchEngine searchEngine = new SearchEngine();
        
        private OnlineEdit onlineEdit = new OnlineEdit();
        
        private ScanVirus scanVirus = new ScanVirus();
        
        public SearchEngine getSearchEngine()
        {
            return searchEngine;
        }
        
        public void setSearchEngine(SearchEngine searchEngine)
        {
            this.searchEngine = searchEngine;
        }
        
        public OnlineEdit getOnlineEdit()
        {
            return onlineEdit;
        }
        
        public void setOnlineEdit(OnlineEdit onlineEdit)
        {
            this.onlineEdit = onlineEdit;
        }
        
        public ScanVirus getScanVirus()
        {
            return scanVirus;
        }
        
        public void setScanVirus(ScanVirus scanVirus)
        {
            this.scanVirus = scanVirus;
        }
        
    }
    
    /**
     * online edit office
     * 
     * @author x90003207
     * 
     */
    public class OnlineEdit
    {
        /** 是否在线编辑 */
        private Boolean available;
        
        public Boolean getAvailable()
        {
            return available;
        }
        
        public void setAvailable(Boolean available)
        {
            this.available = available;
        }
    }
    
    /**
     * scan virus
     * 
     * @author x90003512
     * 
     */
    public class ScanVirus
    {
        /** 是否在线杀毒 */
        private Boolean available;
        
        public Boolean getAvailable()
        {
            return available;
        }
        
        public void setAvailable(Boolean available)
        {
            this.available = available;
        }
    }
    
    /**
     * search engine
     * 
     * @author c90003207
     * 
     */
    public class SearchEngine
    {
        /** 是否全文检索 */
        private Boolean available;
        
        /** 是否摘要 */
        private String needSummary;
        
        public Boolean getAvailable()
        {
            return available;
        }
        
        public void setAvailable(Boolean available)
        {
            this.available = available;
        }
        
        public void setNeedSummary(String needSummary)
        {
            this.needSummary = needSummary;
        }
        
        public String getNeedSummary()
        {
            return needSummary;
        }
    }
    
}
