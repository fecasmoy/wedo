package com.wedo.businessserver.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.SearchException;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.service.WOnlineAbstractServiceImpl;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;

/**
 * 抽象索引工厂方法类，可迭代采用不同策略的分词器
 * 
 * @author c90003207
 * 
 */
public abstract class IndexWriterFactory
    extends WOnlineAbstractServiceImpl
{

    /**
     * 索引文件位置
     */
    private static final File INDEX_FILE = new File(Constants
        .getIndexRoot());

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(IndexWriterFactory.class);

    /**
     * 临时内存索引
     */
    private static RAMDirectory ramDirectory = null;

    /**
     * 全量的Searcher
     */
    private static List<IndexSearcher> fdiskIndexSearcher = null;

    @SuppressWarnings("deprecation")
    private static void init(Integer i)
        throws Exception
    {
        File file = INDEX_FILE
            .listFiles()[i];
        // 判断是否有索引文件
        if (file
            .exists() && file
            .isDirectory() && file
            .listFiles().length > 0)
        {
            try
            {
                IndexReader indexReader = IndexReader
                    .open(file);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                indexSearcher
                    .setSimilarity(new IKSimilarity());
                fdiskIndexSearcher
                    .add(indexSearcher);
            }
            catch (Exception e)
            {
                logger
                    .error(e
                        .getMessage(), e);
            }
        }
    }

    /**
     * 单例化查询器
     * 
     * @return list
     * @throws BusinessException Business Exception
     */
    protected static synchronized List<IndexSearcher> getFdiskIndexSearcherInstance()
        throws BusinessException
    {
        try
        {
            if (fdiskIndexSearcher == null)
            {
                fdiskIndexSearcher = new ArrayList<IndexSearcher>();
                if (!INDEX_FILE
                    .exists())
                {
                    @SuppressWarnings("unused")
                    Boolean flag = INDEX_FILE
                        .mkdirs();
                }
                for (int i = 0; i < INDEX_FILE
                    .listFiles().length; i++)
                {
                    init(i);
                }

            }
            return fdiskIndexSearcher;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00588_1"));
            throw new SearchException("ERROR.00588", e);
        }
    }

    /**
     * 读取PPT文件
     * 
     * @param file file
     * @return String
     * @throws BusinessException Business Exception
     */
    protected static String readPpt(File file)
        throws BusinessException
    {
        try
        {
            // 文档内容
            StringBuffer content = new StringBuffer("");
            SlideShow ss = new SlideShow(new FileInputStream(file));
            // 获得每一张幻灯片
            Slide[] slides = ss
                .getSlides();
            for (int i = 0; i < slides.length; i++)
            {
                TextRun[] truns = slides[i]
                    .getTextRuns();
                for (int k = 0; k < truns.length; k++)
                {
                    content
                        .append(truns[k]
                            .getText());
                }
            }
            return content
                .toString().trim();
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00590"));
            throw new SearchException("ERROR.00590", e);
        }

    }

    /**
     * 读取word文件内容
     * 
     * @param file 文件路径
     * @return String
     * @throws BusinessException Business Exception
     */
    protected static String readWord(File file)
        throws BusinessException
    {
        FileInputStream in = null;
        WordExtractor extractor = null;
        try
        {
            in = new FileInputStream(file);
            String text = null;
            // 创建WordExtractor
            extractor = new WordExtractor(in);
            // 对DOC文件进行提取
            text = extractor
                .getText();
            return text;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00591"));
            throw new SearchException("ERROR.00591", e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in
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

    @SuppressWarnings("deprecation")
    private static void readRow(HSSFRow aRow, StringBuffer content)
        throws Exception
    {
        for (short cellNumOfRow = 0; cellNumOfRow <= aRow
            .getLastCellNum(); cellNumOfRow++)
        {
            if (null != aRow
                .getCell(cellNumOfRow))
            {
                HSSFCell aCell = aRow
                    .getCell(cellNumOfRow);// 获得列值
                try
                {
                    content
                        .append(aCell
                            .getStringCellValue());
                }
                catch (Exception e)
                {
                    logger
                        .error(e
                            .getMessage(), e);
                }
            }
        }
    }

    private static void readLine(HSSFSheet aSheet, StringBuffer content)
        throws Exception
    {
        for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
            .getLastRowNum(); rowNumOfSheet++)
        {
            if (null != aSheet
                .getRow(rowNumOfSheet))
            {
                HSSFRow aRow = aSheet
                    .getRow(rowNumOfSheet); // 获得一个行
                readRow(aRow, content);
            }
        }
    }

    /**
     * 读取Excel文件
     * 
     * @param file file
     * @return string
     * @throws BusinessException Business Exception
     */
    protected static String readExcel(File file)
        throws BusinessException
    {
        try
        {
            // 文档内容
            StringBuffer content = new StringBuffer("");
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            for (int numSheets = 0; numSheets < workbook
                .getNumberOfSheets(); numSheets++)
            {
                if (null != workbook
                    .getSheetAt(numSheets))
                {
                    HSSFSheet aSheet = workbook
                        .getSheetAt(numSheets);// 获得一个sheet
                    readLine(aSheet, content);
                }
            }
            return content
                .toString().trim();
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00592"));
            throw new SearchException("ERROR.00592", e);
        }

    }

    /**
     * 读取PDF文档
     * 
     * @param file file
     * @return String
     * @throws BusinessException Business Exception
     */
    @SuppressWarnings("deprecation")
    protected static String readPdf(File file)
        throws BusinessException
    {
        FileInputStream fis = null;
        COSDocument cos = null;
        try
        {
            StringBuffer content = new StringBuffer("");// 文档内容
            fis = new FileInputStream(file);
            PDFParser p = new PDFParser(fis);
            p
                .parse();
            PDFTextStripper ts = new PDFTextStripper();
            cos = p
                .getDocument();
            content
                .append(ts
                    .getText(cos));
            fis
                .close();
            cos
                .close();
            return content
                .toString().trim();
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00593"));
            throw new SearchException("ERROR.00593", e);
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis
                        .close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e
                        .printStackTrace();
                }
            }
            if (cos != null)
            {
                try
                {
                    cos
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

    private String splitFolderInit(Integer i, String fodler[])
        throws Exception
    {
        String first = null;
        if (i == 0)
        {
            first = fodler[i];
        }
        else
        {
            for (int m = 0; m <= i; m++)
            {
                if (m == 0)
                {
                    first = fodler[m];
                }
                else
                {
                    first += fodler[m];
                }
            }
        }
        return first;
    }

    /**
     * 分割文件夹组成索引项
     * 
     * @param foldertreepath folder tree path
     * @return list
     * @throws BusinessException Business Exception
     */
    protected List<String> splitFolder(String foldertreepath)
        throws BusinessException
    {
        try
        {
            List<String> list = new ArrayList<String>();
            String fodler[] = foldertreepath
                .split(";");
            for (int i = 0; i < fodler.length; i++)
            {
                list
                    .add(splitFolderInit(i, fodler));
            }
            return list;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00589"));
            throw new SearchException("ERROR.00589", e);
        }

    }

    /**
     * 获得磁盘文件
     * 
     * @param filePath filePath
     * @return File
     * @throws BusinessException Exception
     */
    protected File getFile(String filePath)
        throws BusinessException
    {
        try
        {
            File realFile = null;
            List<WSupportNode> list =
                getWSupportByIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            // 获取文件真实路径，有多个挂载的时候
            for (int i = 0; i < list
                .size(); i++)
            {
                String root = list
                    .get(i).getMountPath();
                if (new File(root + filePath)
                    .exists() && new File(root + filePath)
                        .isFile())
                {
                    realFile = new File(root + filePath);
                    break;
                }

            }
            if (realFile == null)
            {
                logger
                    .info(MSG
                        .getString("ERROR.00587"));
                throw new BusinessException("ERROR.00587");
            }
            return realFile;
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00587"));
            throw new SearchException("ERROR.00587", e);
        }

    }

    /**
     * 合并索引
     * 
     * @throws BusinessException Business Exception
     */
    protected abstract void ramToDisk()
        throws BusinessException;

    /**
     * 重置打开索引
     */
    public synchronized void fdiskIndexSearcherInital()
    {
        try
        {
            if (fdiskIndexSearcher != null)
            {
                for (int i = 0; i < fdiskIndexSearcher
                    .size(); i++)
                {
                    fdiskIndexSearcher
                        .get(i).close();
                }
                fdiskIndexSearcher = null;
            }
        }
        catch (Exception e)
        {
            e
                .printStackTrace();
        }

    }

    /**
     * 创建全新索引
     * 
     * @param fileList 文件路径list
     * @param tempFile 临时索引目录
     * @param flag 是否增量
     * @throws BusinessException Business Exception
     */
    public abstract void createNewIndex(List<WFile> fileList, File tempFile,
        Boolean flag)
        throws BusinessException;

    /**
     * 增量索引
     * 
     * @param wfile file
     * @throws BusinessException Business Exception
     */
    public abstract void incrementIndex(WFile wfile)
        throws BusinessException;

    /**
     * 删除索引
     * 
     * @param wfile file
     * @throws BusinessException Business Exception
     */
    public abstract void deleteIndex(WFile wfile)
        throws BusinessException;

    /**
     * 全文检索主方法
     * 
     * @param keyWord 关键字列表
     * @param folderPath 文件夹路径
     * @param filePageInfo 分页对象
     * @param map map
     * @throws BusinessException Business Exception
     */
    public abstract void search(String[] keyWord, String folderPath,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException;

    /**
     * getrAMDIRECTORY
     * 
     * @return the rAMDIRECTORY
     */
    public static RAMDirectory getrAMDIRECTORY()
    {
        return ramDirectory;
    }

    /**
     * set rAMDIRECTORY
     * 
     * @param rAMDIRECTORY the rAMDIRECTORY to set
     */
    public static void setrAMDIRECTORY(RAMDirectory rAMDIRECTORY)
    {
        IndexWriterFactory.ramDirectory = rAMDIRECTORY;
    }

    /**
     * get fDISKINDEXSEARCHER
     * 
     * @return the fDISKINDEXSEARCHER
     */
    public static List<IndexSearcher> getfDISKINDEXSEARCHER()
    {
        return fdiskIndexSearcher;
    }

    /**
     * set fDISKINDEXSEARCHER
     * 
     * @param fDISKINDEXSEARCHER the fDISKINDEXSEARCHER to set
     */
    public static void setfDISKINDEXSEARCHER(
        List<IndexSearcher> fDISKINDEXSEARCHER)
    {
        IndexWriterFactory.fdiskIndexSearcher = fDISKINDEXSEARCHER;
    }

    /**
     * get Index file
     * 
     * @return the indexfile
     */
    public static File getIndexfile()
    {
        return INDEX_FILE;
    }

}
