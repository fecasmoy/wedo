package com.wedo.businessserver.attemper.job;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.attemper.util.DeleteFileUtil;
import com.wedo.businessserver.common.dao.support.PageInfo;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.searchengine.Constants;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.searchengine.SearchContext;

/**
 * 工作计划创建索引,
 * 主要是用于晚上的重新建立索引
 * 因为分布式环境下不可能做到索引的同步处理 处理分为两个部分，
 * 对最新文件的索引和对历史文件的索引
 * 如果含有多个分词器，可选择不同的分析器进行分析并进行索引
 * 
 * @author c90003207
 * 
 */
public class SearchEngineCreateIndexJobImpl
    implements Serializable
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -7598641772798791704L;
    
    /** 日志 */
    private static final Log logger =
        LogFactory.getLog(SearchEngineCreateIndexJobImpl.class);
    
    /**
     * 每页1000条
     */
    private static final int COUNT_PER_PAGE_1000 = 1000;
    
    
    /**
     * 从spring容器中获得此文件处理service
     * 
     * @return WFileService
     */
    private WFileService getWFileService()
    {
        WFileService wFileService = (WFileService) BaseStaticContextLoader.getApplicationContext()
                .getBean("wFileService");// 托管给spring进行处理
        return wFileService;
    }
    
    /**
     * 从spring容器中获得此文件夹处理service
     * 
     * @return
     */
    private WFolderService getWfolderService()
    {
        WFolderService wfolderService = (WFolderService) BaseStaticContextLoader.getApplicationContext()
                .getBean("wFolderService");// 托管给spring进行处理
        return wfolderService;
    }
    
    /**
     * 创建索引 此处待完善异常情况处理
     * 
     * @throws Exception exception
     */
    public void createIndex()
        throws Exception
    {
        ResourceBundle msg = LanguageUtil.getMessage();// 本地化处理
        try
        {
            logger.info(msg.getString("msg.search.index_timer"));// 开始处理定时索引文件
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("avaible", "0");// 可用的文件
            map.put("mergeFlag", "0");// 不是增量存储的文件
            map.put("encryptFlag", "0");// 非加密文件
            map.put("compressFlag", "0");// 非压缩文件
            int currentpage = 1;
            SearchContext searchContext =
                new SearchContext(new IkIndexWriter());// 策略模式制定分词
            File tempFile =
                new File(Constants.getIndexRoot()
                    + DateUtils.format(Calendar.getInstance(), "yyyyMMdd")
                    + "/index");// 索引目录
            Boolean flag = false;
            logger.info(msg.getString("msg.search.indexnewfile"));// 开始索引最新文件
            /** 索引最新文件 */
            while (true)
            {
                try
                {
                    PageInfo<WFile> pageInfo = new PageInfo<WFile>();
                    pageInfo.setCountOfCurrentPage(COUNT_PER_PAGE_1000);// 每页1000条
                    pageInfo.setCurrentPage(currentpage);// 当前页设置
                    getWFileService().pageWFle(map, pageInfo);// 分页查询记录
                    List<WFile> list = new ArrayList<WFile>();
                    for (int i = 0; i < pageInfo.getPageResults().size(); i++)
                    {
                        WFile wfile = pageInfo.getPageResults().get(i);// 获取当前对象
                        try
                        {
                            HashMap<String, String> folderMap =
                                new HashMap<String, String>();
                            folderMap.put("guid", wfile.getFolderguid());// 根据文件的所属文件夹id查询
                            wfile.setFoldertreepath(getWfolderService()
                                .getFolder(folderMap).getTreepath());// 设置索引目录
                            list.add(wfile);// 逐条添加近列表，索引的时候批量索引
                        }
                        catch (Exception e)
                        {
                            logger.info(msg.getString("msg.file")
                                + wfile.getFName()
                                + msg.getString("msg.indexfailed"));// 索引失败
                        }
                        
                    }
                    if (list.size() > 0)
                    {
                        if (tempFile.exists())
                        {
                            long indexSize =
                            		com.wedo.businessserver.searchengine.Constants.INDEX_SIZE;
                            if (FileUtils.sizeOfDirectory(tempFile) > indexSize)
                            { // 但索引文件大于500M
                                RandomGUID randomGUID = new RandomGUID();
                                tempFile =
                                    new File(Constants.getIndexRoot()
                                        + DateUtils.format(Calendar
                                            .getInstance(), "yyyyMMdd")
                                        + "/index"
                                        + randomGUID.getValueAfterMD5());// 生成新的索引目录存放索引文件
                                flag = false;
                            }
                        }
                        searchContext.createNewIndex(list, tempFile, flag);// 重新创建索引
                        flag = true;
                    }
                    currentpage++;
                    if (currentpage > pageInfo.getTotalPage())
                    {
                        break;// 如果查询完成，跳出循环
                    }
                }
                catch (Exception e)
                {
                    logger.info(msg.getString("ERROR.00571"));// 定时索引最新文件失败
                }
                
            }
            currentpage = 1;
            logger.info(msg.getString("msg.search.indexhistoryfile"));// 开始索引历史文件
            /** 索引历史文件 */
            while (true)
            {
                try
                {
                    PageInfo<WFileHistory> pageInfoWFileHistory =
                        new PageInfo<WFileHistory>();
                    pageInfoWFileHistory
                        .setCountOfCurrentPage(COUNT_PER_PAGE_1000);// 每页1000条
                    pageInfoWFileHistory.setCurrentPage(currentpage);// 当前页设置
                    getWFileService()
                        .pageWFleHistory(map, pageInfoWFileHistory);// 分页查询记录
                    List<WFile> listWFileHistory = new ArrayList<WFile>();
                    for (int i = 0; i < pageInfoWFileHistory.getPageResults()
                        .size(); i++)
                    {
                        WFile file = new WFile();
                        WFileHistory wfileHistory =
                            pageInfoWFileHistory.getPageResults().get(i);
                        try
                        {
                            HashMap<String, String> folderMap =
                                new HashMap<String, String>();
                            folderMap.put("guid", wfileHistory.getFolderguid());
                            wfileHistory.setFoldertreepath(getWfolderService()
                                .getFolder(folderMap).getTreepath());// 设置索引路径
                            BeanUtils.copyProperties(file, wfileHistory);// 拷贝属性
                            listWFileHistory.add(file);// 逐条添加近列表，索引的时候批量索引
                        }
                        catch (Exception e)
                        {
                            logger.info(msg.getString("msg.file")
                                + wfileHistory.getFName()
                                + msg.getString("msg.indexfailed"));// 单个索引错误不影响处理
                        }
                        
                    }
                    if (listWFileHistory.size() > 0)
                    {
                        if (tempFile.exists())
                        {
                            long indexSize =
                            		com.wedo.businessserver.searchengine.Constants.INDEX_SIZE;
                            if (FileUtils.sizeOfDirectory(tempFile) > indexSize)
                            { // 但索引文件大于500M
                                RandomGUID randomGUID = new RandomGUID();
                                tempFile =
                                    new File(Constants.getIndexRoot()
                                        + DateUtils.format(Calendar
                                            .getInstance(), "yyyyMMdd")
                                        + "/index"
                                        + randomGUID.getValueAfterMD5());// 生成新的索引目录存放索引文件
                                flag = false;
                            }
                        }
                        searchContext.createNewIndex(listWFileHistory,
                            tempFile, flag);// 创建索引
                        flag = true;
                    }
                    currentpage++;
                    if (currentpage > pageInfoWFileHistory.getTotalPage())
                    {
                        break;// 如果查询完成，跳出循环
                    }
                }
                catch (Exception e)
                {
                    logger.info(msg.getString("ERROR.00572"));// 定时索引历史文件失败
                }
            }
            File oldFile = new File(Constants.getIndexRoot());// 当前索引目录
            if (oldFile.exists())
            {
                if (DeleteFileUtil.deleteDirectory(Constants.getIndexRoot()) == true)
                { // 删除当前索引目录
                    File file =
                        new File(Constants.getIndexRoot()
                            + DateUtils.format(Calendar.getInstance(),
                                "yyyyMMdd"));
                    if (!file.renameTo(new File(Constants.getIndexRoot()))) // 删除旧索引文件
                    {
                        throw new Exception("error index create");
                    }
                }
                else
                {
                    File file =
                        new File(Constants.getIndexRoot()
                            + DateUtils.format(Calendar.getInstance(),
                                "yyyyMMdd"));
                    @SuppressWarnings("unused")
                    Boolean delflag = file.delete();
                    throw new Exception("error index create");
                }
            }
            else
            {
                File file =
                    new File(Constants.getIndexRoot()
                        + DateUtils.format(Calendar.getInstance(), "yyyyMMdd"));
                if (!file.renameTo(new File(Constants.getIndexRoot()))) // 删除旧索引文件
                {
                    throw new Exception("error index create");
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
