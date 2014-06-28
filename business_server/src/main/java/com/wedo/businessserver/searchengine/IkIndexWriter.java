package com.wedo.businessserver.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.SearchException;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;
import com.wedo.businessserver.css3.ws.model.SFile;
import com.wedo.businessserver.css3.ws.model.SFileHitory;
import com.wedo.businessserver.heartbeat.ClientFactory;

/**
 * Ik分词算法实现类
 * 
 * @author c90003207
 * 
 */
@SuppressWarnings("deprecation")
public class IkIndexWriter
    extends IndexWriterFactory
{

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(IkIndexWriter.class);

    /**
     * SIZE 1024
     */
    private static final int SIZE_1024 = 1024;

    /**
     * WAIT MICR-SCOND 5000
     */
    private static final int WAIT_MICR_SCOND_5000 = 5000;

    /**
     * OFFICE类型
     */
    private static final String OFFICE = "doc,dot,ppt,pot,pps,xls,xlt";

    /**
     * WORD类型
     */
    private static final String WORD = "doc,dot";

    /**
     * PPT类型
     */
    private static final String PPT = "ppt,pot,pps";

    /**
     * EXCEL类型
     */
    private static final String EXCEL = "xls,xlt";

    /**
     * IMAGE类型
     */
    private static final String IMAGE = "jpg,jpeg,gif,bmp,png";

    /**
     * MUSIC类型
     */
    private static final String MUSIC = "mp3,wav,wma,wm,midi,mid";

    /**
     * VEDIO类型
     */
    private static final String VEDIO = "avi,mpg,rm,wmv,mpeg,mp4";

    /**
     * msg
     */
    private ResourceBundle msg = LanguageUtil
        .getMessage();

    private String parseContent(String fileName, File file)
        throws Exception
    {
        try
        {
            String fileContent = "";
            if (fileName
                .contains("."))
            {
                String lastName = fileName
                    .substring(fileName
                        .lastIndexOf(".") + 1, fileName
                        .length());
                if (WORD
                    .indexOf(lastName) != -1)
                {
                    fileContent = readWord(file);
                }
                else if (PPT
                    .indexOf(lastName) != -1)
                {
                    fileContent = readPpt(file);
                }
                else if (EXCEL
                    .indexOf(lastName) != -1)
                {
                    fileContent = readExcel(file);
                }
                else if (lastName
                    .equalsIgnoreCase("pdf"))
                {
                    fileContent = readPdf(file);
                }
                else if (lastName
                    .equalsIgnoreCase("txt"))
                {
                    fileContent = txtFileToString(file);
                }
            }
            return fileContent;
        }
        catch (Exception e)
        {
            throw e;
        }

    }

    /**
     * 读出txt言语件为字符串
     * 
     * @param file File
     * 
     * @return String
     * @throws Exception 文件操作异常
     * 
     */
    private String txtFileToString(File file)
        throws Exception
    {
        BufferedReader br = null;
        try
        {
            String result = null;
            StringBuffer buffString = new StringBuffer("");
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br
                .readLine()) != null)
            {
                buffString
                    .append(line);
            }
            br
                .close();
            result = buffString
                .toString();

            return result;
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (br != null)
            {
                br
                    .close();
            }
        }

    }

    private void indexFileContent(String fileName, File file, Document doc)
        throws Exception
    {
        try
        {
            if (file
                .canRead())
            {
                String fileContent = parseContent(fileName, file);
                if (StringUtils
                    .isNotBlank(fileContent))
                {
                    // 加入文件内容作为索引条件
                    doc
                        .add(new Field("fileContent", fileContent,
                            Field.Store.COMPRESS, Field.Index.ANALYZED));
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }

    }

    private void indexFileType(String fileName, Document doc)
        throws Exception
    {
        if (fileName
            .contains("."))
        {
            String lastName = fileName
                .substring(fileName
                    .lastIndexOf(".") + 1, fileName
                    .length());
            // 加入类型
            if (OFFICE
                .indexOf(lastName) != -1)
            {
                doc
                    .add(new Field("fileType", Constants.OFFICE_LABEL,
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            else if (IMAGE
                .indexOf(lastName) != -1)
            {
                doc
                    .add(new Field("fileType", Constants.IMAGE_LABEL,
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            else if (MUSIC
                .indexOf(lastName) != -1)
            {
                doc
                    .add(new Field("fileType", Constants.MUSIC_LABEL,
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            else if (VEDIO
                .indexOf(lastName) != -1)
            {
                doc
                    .add(new Field("fileType", Constants.VEDIO_LABEL,
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            else
            {
                doc
                    .add(new Field("fileType", Constants.OTHER_LABEL,
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
        }
        else
        {
            doc
                .add(new Field("fileType", Constants.OTHER_LABEL,
                    Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }

    /**
     * add index
     * 
     * @param wfile file
     * @return document
     * @throws BusinessException Business Exception
     */
    private Document addIndex(WFile wfile)
        throws BusinessException
    {
        File file = null;
        try
        {
            String fileName = wfile
                .getFName();
            // 获得磁盘文件
            file = getFile(wfile
                .getFpath());
            Document doc = new Document();
            String folderTreePath = wfile
                .getFoldertreepath();
            // 获得所有可能为父的可能
            List<String> list = splitFolder(folderTreePath);
            for (int i = 0; i < list
                .size(); i++)
            {
                // 加入文件夹作为索引条件
                doc
                    .add(new Field("folder" + i, list
                        .get(i), Field.Store.NO, Field.Index.ANALYZED));
            }
            if (wfile
                .getFlag() != null)
            {
                // 说明是历史版本的
                doc
                    .add(new Field("fileNew", "false", Field.Store.NO,
                        Field.Index.NOT_ANALYZED));
            }
            else
            {
                doc
                    .add(new Field("fileNew", "true", Field.Store.NO,
                        Field.Index.NOT_ANALYZED));
            }
            doc
                .add(new Field("fileVersion", wfile
                    .getVersion(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            // 加入文件uuid
            doc
                .add(new Field("fileUuid", wfile
                    .getFileuuid(), Field.Store.YES, Field.Index.ANALYZED));
            // 加入文件夹GUID
            doc
                .add(new Field("folderGuid", wfile
                    .getFolderguid(), Field.Store.NO, Field.Index.ANALYZED));
            // 加入文件uuid
            doc
                .add(new Field("fileUuidVersion", wfile
                    .getFileuuid() + wfile
                    .getVersion(), Field.Store.NO, Field.Index.ANALYZED));
            // 加入文件时间
            doc
                .add(new Field("fileTime", DateUtils
                    .format(wfile
                        .getFileCreateTime(), "yyyyMMddHHmmss"),
                    Field.Store.YES, Field.Index.NOT_ANALYZED));
            // 加入文件时间作为索引条件
            doc
                .add(new Field("fileDate", DateTools
                    .dateToString(wfile
                        .getFileCreateTime().getTime(),
                        DateTools.Resolution.MINUTE), Field.Store.NO,
                    Field.Index.NOT_ANALYZED));
            // 加入文件大小作为索引条件
            doc
                .add(new Field("fileSize", wfile
                    .getFileSize().toString(), Field.Store.YES,
                    Field.Index.NOT_ANALYZED));
            // 加入文件名作为索引条件
            doc
                .add(new Field("fileName", fileName, Field.Store.YES,
                    Field.Index.ANALYZED));
            indexFileType(fileName, doc);
            indexFileContent(fileName, file, doc);
            return doc;
        }
        catch (Exception e)
        {
            throw new SearchException("ERROR.00116", e);
        }
    }

    /**
     * 全量索引
     * 
     * @param fileList file List
     * @param tempFile temp File
     * @param flag flag
     * @throws BusinessException Business Exception
     */
    @Override
    public void createNewIndex(List<WFile> fileList, File tempFile, Boolean flag)
        throws BusinessException
    {
        Analyzer analyzer = new IKAnalyzer();
        IndexWriter iwriter = null;
        try
        {
            if (!tempFile
                .exists())
            {
                @SuppressWarnings("unused")
                // 创建目录
                Boolean mkflag = tempFile
                    .mkdirs();
            }
            if (flag == false)
            {
                // 索引采用磁盘方式
                iwriter =
                    new IndexWriter(tempFile, analyzer, true,
                        IndexWriter.MaxFieldLength.LIMITED);
            }
            else
            {
                // 索引采用磁盘方式
                iwriter =
                    new IndexWriter(tempFile, analyzer, false,
                        IndexWriter.MaxFieldLength.LIMITED);
            }
            for (WFile wfile : fileList)
            {
                try
                {
                    Document doc = addIndex(wfile);
                    iwriter
                        .addDocument(doc);
                    logger
                        .debug("msg.file" + wfile
                            .getFName() + msg
                            .getString("msg.file.index_s"));
                }
                catch (Exception e)
                {
                    logger
                        .info("msg.file" + wfile
                            .getFName() + msg
                            .getString("msg.file.index_s"));
                }
            }
            iwriter
                .optimize();
            iwriter
                .close();
            if (getfDISKINDEXSEARCHER() != null)
            {
                for (int i = 0; i < getfDISKINDEXSEARCHER()
                    .size(); i++)
                {
                    getfDISKINDEXSEARCHER()
                        .get(i).close();
                }
                setfDISKINDEXSEARCHER(null);
            }

        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("ERROR.00581"));
            throw new SearchException("ERROR.00581", e);
        }
        finally
        {
            if (iwriter != null)
            {
                try
                {
                    iwriter
                        .close();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e
                        .printStackTrace();
                }
            }
        }

    }

    /**
     * 增量索引
     * 
     * @param wfile file
     * @throws BusinessException Business Exception
     */
    @Override
    public synchronized void incrementIndex(WFile wfile)
        throws BusinessException
    {
        IndexWriter indexWriter = null;
        try
        {
            Analyzer analyzer = new IKAnalyzer();
            WFile tempWfile = null;
            if (wfile
                .getWFileHistory() != null)
            {
                tempWfile = new WFile();
                tempWfile
                    .setFileuuid(wfile
                        .getFileuuid());
                tempWfile
                    .setVersion(wfile
                        .getWFileHistory().getVersion());
                deleteIndex(tempWfile);
            }
            if (getrAMDIRECTORY() == null)
            {
                // 第一次采用全量内存索引
                setrAMDIRECTORY(new RAMDirectory());
                indexWriter =
                    new IndexWriter(getrAMDIRECTORY(), analyzer, true,
                        IndexWriter.MaxFieldLength.LIMITED);
            }
            else
            {
                // 增量索引
                indexWriter =
                    new IndexWriter(getrAMDIRECTORY(), analyzer, false,
                        IndexWriter.MaxFieldLength.LIMITED);
            }
            // 增加目前最新的版本
            Document doc = addIndex(wfile);
            indexWriter
                .addDocument(doc);
            if (wfile
                .getWFileHistory() != null)
            {
                BeanUtils
                    .copyProperties(tempWfile, wfile
                        .getWFileHistory());
                tempWfile
                    .setVersion(wfile
                        .getWFileHistory().getVersion());
                tempWfile
                    .setFlag(true);
                indexWriter
                    .addDocument(addIndex(tempWfile));
            }
        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("ERROR.00582") + wfile
                    .getFpath());
            e
                .printStackTrace();
            throw new SearchException("ERROR.00582", e);
        }
        finally
        {
            if (indexWriter != null)
            {
                try
                {
                    indexWriter
                        .close();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e
                        .printStackTrace();
                }
            }
            if (getrAMDIRECTORY() != null && getrAMDIRECTORY()
                .sizeInBytes() > SIZE_1024 * SIZE_1024)
            {
                ramToDisk();
            }
        }
    }

    private List<SFileHitory> searchHistory(String[] keyWord, String fileuuid)
    {
        List<SFileHitory> list = new ArrayList<SFileHitory>();
        IndexSearcher[] searchers = null;
        try
        {
            Hits topDocs = null;
            List<IndexSearcher> listsearchers = getFdiskIndexSearcherInstance();
            if (listsearchers == null || listsearchers
                .size() == 0)
            {
                return list;
            }
            searchers = new IndexSearcher[listsearchers
                .size()];
            for (int i = 0; i < listsearchers
                .size(); i++)
            {
                searchers[i] = listsearchers
                    .get(i);
            }
            BooleanQuery query = new BooleanQuery();
            TermQuery isNewFile = new TermQuery(new Term("fileNew", "false"));
            TermQuery queryuuid = new TermQuery(new Term("fileUuid", fileuuid));
            query
                .add(queryuuid, BooleanClause.Occur.MUST);
            query
                .add(isNewFile, BooleanClause.Occur.MUST);
            if (keyWord != null && keyWord.length > 0)
            {
                if (StringUtils
                    .isNotBlank(keyWord[0]))
                {
                    String[] fieldsContent = {"fileContent", "fileName"};
                    QueryParser parseContent =
                        new MultiFieldQueryParser(fieldsContent,
                            new IKAnalyzer());
                    parseContent
                        .setDefaultOperator(Operator.OR);
                    Query queryContent = parseContent
                        .parse(keyWord[0]);
                    query
                        .add(queryContent, BooleanClause.Occur.MUST);
                }
            }
            // 多索引合并查询
            MultiSearcher multiSearcher = new MultiSearcher(searchers);
            topDocs = multiSearcher
                .search(query);
            if (topDocs != null)
            {
                for (int i = 0; i < topDocs
                    .length(); i++)
                {
                    Document targetDoc = topDocs
                        .doc(i);
                    SFileHitory sFileHitory = new SFileHitory();
                    // 设置uuid
                    sFileHitory
                        .setFileuuid(targetDoc
                            .getField("fileUuid").stringValue());
                    // 设置文件时间
                    sFileHitory
                        .setLastModifyTime(targetDoc
                            .getField("fileTime").stringValue());
                    // 设置文件大小
                    sFileHitory
                        .setFileSize(Long
                            .parseLong(targetDoc
                                .getField("fileSize").stringValue()));
                    // 设置文件名称
                    sFileHitory
                        .setName(targetDoc
                            .getField("fileName").stringValue());
                    sFileHitory
                        .setVersion(targetDoc
                            .getField("fileVersion").stringValue());
                    list
                        .add(sFileHitory);
                }
            }

        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("ERROR.00583") + " : " + fileuuid);
            // TODO: handle exception
        }
        finally
        {
            closeSearch(searchers);
        }
        return list;
    }

    private List<SFile> searchNewSetList(Integer startlength,
        Integer endLength, String[] keyWord, Hits topDocs,
        Highlighter highlighter)
        throws Exception
    {
        List<SFile> list = new ArrayList<SFile>();
        for (int i = startlength; i < endLength; i++)
        {
            Document targetDoc = topDocs
                .doc(i);
            SFile sFile = new SFile();
            String fileFrom = targetDoc
                .get("fileFrom");
            if (StringUtils
                .isNotBlank(fileFrom))
            {
                sFile
                    .setFileFrom(fileFrom);
            }
            String fileSendTo = targetDoc
                .get("fileSendTo");
            if (StringUtils
                .isNotBlank(fileSendTo))
            {
                sFile
                    .setSendTo(fileSendTo);
            }
            String fileNoteUrl = targetDoc
                .get("fileNoteUrl");
            if (StringUtils
                .isNotBlank(fileNoteUrl))
            {
                sFile
                    .setFileNoteUrl(fileNoteUrl);
            }
            // 设置uuid
            sFile
                .setFileuuid(targetDoc
                    .getField("fileUuid").stringValue());
            // 设置文件时间
            sFile
                .setLastModifyTime(DateUtils
                    .format(DateUtils
                        .parse(targetDoc
                            .getField("fileTime").stringValue(),
                            "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss"));
            // 设置文件大小
            sFile
                .setFileSize(Long
                    .parseLong(targetDoc
                        .getField("fileSize").stringValue()));
            // 设置文件名称
            sFile
                .setName(targetDoc
                    .getField("fileName").stringValue());
            sFile
                .setVersion(targetDoc
                    .getField("fileVersion").stringValue());
            sFile
                .setSFileHitorys(searchHistory(keyWord, targetDoc
                    .getField("fileUuid").stringValue()));
            sFile
                .setFileType(targetDoc
                    .getField("fileType").stringValue());
            String fileContent = targetDoc
                .get("fileContent");
            if (StringUtils
                .isNotBlank(fileContent)
                && (keyWord != null && keyWord.length > 0))
            {
                fileContent = fileContent
                    .replaceAll("</", "");
                String highLightText =
                    highlighter
                        .getBestFragment(new IKAnalyzer(), "fileContent",
                            fileContent);
                sFile
                    .setHilight(highLightText);
            }
            list
                .add(sFile);
        }
        return list;
    }

    private BooleanQuery queryDate(String starttime, String endtime,
        BooleanQuery query)
        throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils
            .isNotBlank(starttime) && StringUtils
            .isNotBlank(endtime))
        {
            Date startDate = sdf
                .parse(starttime);
            Date endDate = sdf
                .parse(endtime);
            String startDateStr = DateTools
                .dateToString(startDate, DateTools.Resolution.MINUTE);
            String endDateStr = DateTools
                .dateToString(endDate, DateTools.Resolution.MINUTE);
            Term start = new Term("fileDate", startDateStr);
            Term end = new Term("fileDate", endDateStr);
            RangeQuery rangeQuery = new RangeQuery(start, end, true);
            query
                .add(rangeQuery, BooleanClause.Occur.MUST);
        }
        else if (StringUtils
            .isNotBlank(starttime) && StringUtils
            .isBlank(endtime))
        {
            Date startDate = sdf
                .parse(starttime);
            String startDateStr = DateTools
                .dateToString(startDate, DateTools.Resolution.MINUTE);
            String endDateStr = DateTools
                .dateToString(Calendar
                    .getInstance().getTime(), DateTools.Resolution.MINUTE);
            Term start = new Term("fileDate", startDateStr);
            Term end = new Term("fileDate", endDateStr);
            RangeQuery rangeQuery = new RangeQuery(start, end, true);
            query
                .add(rangeQuery, BooleanClause.Occur.MUST);
        }
        else if (StringUtils
            .isBlank(starttime) && StringUtils
            .isNotBlank(endtime))
        {
            Date startDate = sdf
                .parse("1990-01-01");
            Date endDate = sdf
                .parse(endtime);
            String startDateStr = DateTools
                .dateToString(startDate, DateTools.Resolution.MINUTE);
            String endDateStr = DateTools
                .dateToString(endDate, DateTools.Resolution.MINUTE);
            Term start = new Term("fileDate", startDateStr);
            Term end = new Term("fileDate", endDateStr);
            RangeQuery rangeQuery = new RangeQuery(start, end, true);
            query
                .add(rangeQuery, BooleanClause.Occur.MUST);
        }
        return query;
    }

    private Integer parseSatrt(Integer startlength, Integer totalcount)
        throws Exception
    {
        if (startlength >= totalcount)
        {
            startlength = totalcount - 1;
            if (startlength < 0)
            {
                startlength = 0;
            }
        }
        return startlength;
    }

    private void queryKeyWord(String[] keyWord, BooleanQuery query,
        BooleanQuery queryHighlight)
        throws Exception
    {
        if (keyWord != null && keyWord.length > 0)
        {
            if (StringUtils
                .isNotBlank(keyWord[0]))
            {
                String[] fieldsContent =
                {"fileContent", "fileName", "fileFrom"};
                QueryParser parseContent =
                    new MultiFieldQueryParser(fieldsContent, new IKAnalyzer());
                parseContent
                    .setDefaultOperator(Operator.OR);
                Query queryContent = parseContent
                    .parse(keyWord[0]);
                query
                    .add(queryContent, BooleanClause.Occur.MUST);
                queryHighlight
                    .add(queryContent, BooleanClause.Occur.MUST);
            }
        }
    }

    private void closeSearch(IndexSearcher[] searchers)
    {
        if (searchers != null)
        {
            for (int i = 0; i < searchers.length; i++)
            {
                try
                {
                    searchers[i]
                        .close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e
                        .printStackTrace();
                }
            }
        }
    }

    /**
     * 查询最新记录
     * 
     * @param keyWord
     * @param folderPath
     * @param filePageInfo
     * @param flag
     * @throws BusinessException
     */
    private void searchNew(String[] keyWord, String folderPath,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException
    {
        IndexSearcher[] searchers = null;
        try
        {
            Hits topDocs = null;
            List<IndexSearcher> listsearchers = getFdiskIndexSearcherInstance();
            if (listsearchers == null || listsearchers
                .size() == 0)
            {
                filePageInfo
                    .setTotalCount(0);
                return;
            }
            searchers = new IndexSearcher[listsearchers
                .size()];
            for (int i = 0; i < listsearchers
                .size(); i++)
            {
                searchers[i] = listsearchers
                    .get(i);
            }
            // 获得分页起始位置
            Integer startlength = filePageInfo
                .getCountOfCurrentPage() * (filePageInfo
                .getCurrentPage() - 1);
            // 获得分页结束位置
            Integer endLength = filePageInfo
                .getCountOfCurrentPage() * filePageInfo
                .getCurrentPage();
            BooleanQuery query = new BooleanQuery();
            BooleanQuery queryHighlight = new BooleanQuery();
            String filetype = map
                .get("filetype");
            String starttime = map
                .get("starttime");
            String endtime = map
                .get("endtime");
            queryKeyWord(keyWord, query, queryHighlight);
            if (StringUtils
                .isNotBlank(filetype))
            {
                TermQuery isFiletype =
                    new TermQuery(new Term("fileType", filetype));
                query
                    .add(isFiletype, BooleanClause.Occur.MUST);
            }
            query = queryDate(starttime, endtime, query);
            int length = folderPath
                .split("/").length - 1;
            String[] fieldsFolder = new String[length];
            for (int i = 0; i < length; i++)
            {
                fieldsFolder[i] = "folder" + i;
            }
            QueryParser parsefolder =
                new MultiFieldQueryParser(fieldsFolder, new IKAnalyzer());
            parsefolder
                .setDefaultOperator(Operator.OR);
            Query queryFolder = parsefolder
                .parse(folderPath);
            query
                .add(queryFolder, BooleanClause.Occur.MUST);
            TermQuery isNewFile = new TermQuery(new Term("fileNew", "true"));
            query
                .add(isNewFile, BooleanClause.Occur.MUST);
            // 多索引合并查询
            MultiSearcher multiSearcher = new MultiSearcher(searchers);
            topDocs = multiSearcher
                .search(query);
            if (topDocs != null)
            {
                // 总共记录
                Integer totalSize = topDocs
                    .length();
                filePageInfo
                    .setTotalCount(totalSize);
                String prefixHTML = "<font color='red'>";
                String suffixHTML = "</font>";
                SimpleHTMLFormatter simpleHTMLFormatter =
                    new SimpleHTMLFormatter(prefixHTML, suffixHTML);
                Highlighter highlighter =
                    new Highlighter(simpleHTMLFormatter, new QueryScorer(
                        queryHighlight));
                int totalcount = topDocs
                    .length();
                // 总记录数
                filePageInfo
                    .setTotalCount(totalcount);
                startlength = parseSatrt(startlength, totalcount);
                if (endLength > totalcount)
                {
                    endLength = totalcount;
                }
                filePageInfo
                    .setPageResults(searchNewSetList(startlength, endLength,
                        keyWord, topDocs, highlighter));
            }

        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("ERROR.00584"));
            throw new SearchException("ERROR.00584", e);
        }
        finally
        {
            closeSearch(searchers);
        }
    }

    /**
     * 全文检索主方法
     * 
     * @param keyWord 关键字
     * @param folderPath 文件夹path
     * @param filePageInfo 填充对象
     * @param map map
     * @throws BusinessException Business Exception
     */
    @Override
    public void search(String[] keyWord, String folderPath,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException
    {
        searchNew(keyWord, folderPath, filePageInfo, map);
    }

    private void deleteIndexInit(String folderGuid, String fileuuid,
        String version, WFile wfile)
        throws Exception
    {
        IndexReader ramdindexReader = IndexReader
            .open(getrAMDIRECTORY());
        if (StringUtils
            .isNotBlank(folderGuid))
        {
            ramdindexReader
                .deleteDocuments(new Term("folderGuid", folderGuid));
        }
        else
        {
            if (StringUtils
                .isBlank(wfile
                    .getVersion()))
            {
                ramdindexReader
                    .deleteDocuments(new Term("fileUuid", fileuuid));
            }
            else
            {
                ramdindexReader
                    .deleteDocuments(new Term("fileUuidVersion", fileuuid
                        + version));
            }
        }
        ramdindexReader
            .close();
    }

    /**
     * 删除索引
     * 
     * @param wfile file
     * @throws BusinessException Business Exception
     */
    @Override
    public synchronized void deleteIndex(WFile wfile)
        throws BusinessException
    {
        String fileuuid = wfile
            .getFileuuid();
        String version = wfile
            .getVersion();
        String folderGuid = wfile
            .getFolderguid();
        try
        {
            // 删除内存索引文件
            if (getrAMDIRECTORY() != null)
            {
                deleteIndexInit(folderGuid, fileuuid, version, wfile);
            }
        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("ERROR.00585") + " : " + fileuuid);
            throw new SearchException("ERROR.00585", e);
        }
    }

    private File ramToDiskGetFile(String tempPath)
        throws Exception
    {
        File indexFile = null;
        if (tempPath == null)
        {
            RandomGUID randomGUID = new RandomGUID();
            indexFile = new File(getIndexfile()
                .getAbsolutePath() + "/index" + randomGUID
                .getValueAfterMD5());
            if (!indexFile
                .exists())
            {
                Boolean flag = indexFile
                    .mkdirs();
                if (!flag)
                {
                    logger
                        .error("Create Folder failed. folder is: " + indexFile
                            .getPath());
                }
            }
        }
        else
        {
            indexFile = new File(tempPath);
        }
        return indexFile;
    }

    private String ramToDiskGetPath(File[] files)
        throws Exception
    {
        String tempPath = null;
        for (int i = 0; i < files.length; i++)
        {
            File dir = files[i];
            if (dir
                .isDirectory())
            {
                if (FileUtils
                    .sizeOfDirectory(dir) < com.wedo.businessserver.searchengine.Constants.INDEX_SIZE)
                {
                    tempPath = dir
                        .getAbsolutePath();
                    break;
                }
            }
        }
        return tempPath;
    }

    /**
     * 已存在索相目录锁文件情况下，产生新锁文件
     * 
     * @param modify File最近更新时间
     * @param file File
     * 
     * @return File
     * @throws Exception 文件操作异常
     */
    private File newLockFile(Long modify, File file)
        throws Exception
    {
        try
        {
            Long thisTime = System
                .currentTimeMillis();
            if (thisTime - modify > Constants.LOCK_TIME)
            {
                @SuppressWarnings("unused")
                Boolean flag = file
                    .delete();
                @SuppressWarnings("unused")
                // 重新生成
                Boolean creflag = file
                    .createNewFile();
            }
            else
            {
                while (file
                    .exists() && (System
                    .currentTimeMillis() - file
                    .lastModified() < Constants.LOCK_TIME))
                {
                    // 等待,避免同时合并
                    wait(WAIT_MICR_SCOND_5000);
                }
            }
            if (!file
                .exists())
            {
                @SuppressWarnings("unused")
                Boolean flag = file
                    .createNewFile();
            }
            return file;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * 合并索引到磁盤
     * 
     * @throws BusinessException Business Exception
     */
    protected synchronized void ramToDisk()
        throws BusinessException
    {
        IndexWriter fsiwriter = null;
        Analyzer analyzer = new IKAnalyzer();
        File file = null;
        String tempPath = null;
        File indexFile = null;
        try
        {
            File[] files = getIndexfile()
                .listFiles();
            tempPath = ramToDiskGetPath(files);
            indexFile = ramToDiskGetFile(tempPath);
            file = new File(indexFile
                .getAbsolutePath() + "/temp");
            if (!file
                .exists())
            {
                // 如果没有，不需要加文件锁
                @SuppressWarnings("unused")
                Boolean flag = file
                    .createNewFile();
            }
            else
            {
                Long modify = file
                    .lastModified();
                file = newLockFile(modify, file);
            }
            if (getrAMDIRECTORY() != null)
            {
                if (indexFile
                    .listFiles().length == 1)
                {
                    // 索引采用磁盘方式,全新
                    fsiwriter =
                        new IndexWriter(indexFile, analyzer, true,
                            IndexWriter.MaxFieldLength.LIMITED);
                }
                else
                {
                    // 索引采用磁盘方式,合并
                    fsiwriter =
                        new IndexWriter(indexFile, analyzer, false,
                            IndexWriter.MaxFieldLength.LIMITED);
                }
                // 合并索引
                fsiwriter
                    .addIndexes(new Directory[] {getrAMDIRECTORY()});
                fsiwriter
                    .optimize();
                fsiwriter
                    .close();
                getrAMDIRECTORY()
                    .close();
                setrAMDIRECTORY(null);
            }
            ClientFactory
                .run(
                    com.wedo.businessserver.heartbeat.Constants.INDEX_SEARCHER_INITAL,
                    "FdiskIndexSearcher");
            fdiskIndexSearcherInital();
        }
        catch (Exception e)
        {
            throw new SearchException("ERROR.00586", e);
        }
        finally
        {
            if (file != null)
            {
                @SuppressWarnings("unused")
                Boolean flag = file
                    .delete();
            }
            if (fsiwriter != null)
            {
                try
                {
                    fsiwriter
                        .close();
                }
                catch (CorruptIndexException e)
                {
                    e
                        .printStackTrace();
                }
                catch (IOException e)
                {
                    e
                        .printStackTrace();
                }
            }
        }
    }
}
