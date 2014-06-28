package com.wedo.businessserver.storage.jcr.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.wedo.businessserver.common.util.RandomGUID;

/**
 * uuid生成器
 * 
 * @author c90003207
 * 
 */
public class JuuidUtils
{
    /** 最终元数据存放在磁盘上的目录结构 */
    private static final String NODE_PATH_TEMPLATE =
        "xx/xx/xx/xx/xxxxxxxxxxxxxxxxxxxxxxxx";
    
    /** uuid的形态配置 */
    private static final String UUID_PATH_TEMPLATE =
        "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
    
    /** uuid的每个字符的可能选择 */
    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();
    
    private static final int ARRAY_LENGTH_4 = 4;
    
    private static final int ARRAY_LENGTH_16 = 16;
    
    private static final int ARRAY_LENGTH_32 = 32;
    
    private static final int ARRAY_LENGTH_0F = 0x0f;
    
    /**
     * 根据uuid获得文件的实际存储路径
     * 
     * @param uuid uuid
     * @return return value
     * @throws Exception Exception
     */
    public static String getFilePath(String uuid)
        throws Exception
    {
        StringBuffer sb = new StringBuffer();
        char[] chars = uuid.toCharArray();
        int cnt = 0;
        for (int i = 0; i < NODE_PATH_TEMPLATE.length(); i++)
        {
            char ch = NODE_PATH_TEMPLATE.charAt(i);
            if (ch == 'x' && cnt < chars.length)
            {
                ch = chars[cnt++];
                if (ch == '-')
                {
                    ch = chars[cnt++];
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }
    
    /**
     * 获得对象的uuid和元数据存放位置格式
     * 
     * @return return value
     * @throws Exception Exception
     */
    private static Map<String, String> buildNodeFolderPath()
        throws Exception
    {
        StringBuffer sb = new StringBuffer();
        StringBuffer uuidsb = new StringBuffer();
        String s = buildPropFilePath();
        char[] chars = s.toCharArray();
        int cnt = 0;
        for (int i = 0; i < NODE_PATH_TEMPLATE.length(); i++)
        { // 根据生成的uuid进行格式转换
            char ch = NODE_PATH_TEMPLATE.charAt(i);
            if (ch == 'x' && cnt < chars.length)
            {
                ch = chars[cnt++];
                if (ch == '-')
                { // 格式替换生存磁盘路径
                    ch = chars[cnt++];
                }
            }
            sb.append(ch);
        }
        cnt = 0;
        for (int i = 0; i < UUID_PATH_TEMPLATE.length(); i++)
        { // 根据生成的uuid进行格式转换
            char ch = UUID_PATH_TEMPLATE.charAt(i);
            if (ch == 'x' && cnt < chars.length)
            {
                ch = chars[cnt++];
                if (ch == '-')
                { // 格式替换生存uuid的最后形态
                    ch = chars[cnt++];
                }
            }
            uuidsb.append(ch);
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", uuidsb.toString());
        map.put("uuidPath", sb.toString());
        return map;
    }
    
    /**
     * 或其对象的uuid名称
     * 
     * @return return value
     * @throws Exception Exception
     */
    private static synchronized String buildPropFilePath()
        throws Exception
    {
        String fileName;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            RandomGUID randomGUID = new RandomGUID();// 获取一个guid，全局唯一
            md5.update(randomGUID.getValueAfterMD5().getBytes());// 根据md5进行算法处理
            byte[] bytes = md5.digest();
            char[] chars = new char[ARRAY_LENGTH_32];
            for (int i = 0, j = 0; i < ARRAY_LENGTH_16; i++)
            {
                chars[j++] =
                    HEXDIGITS[(bytes[i] >> ARRAY_LENGTH_4) & ARRAY_LENGTH_0F];
                chars[j++] = HEXDIGITS[bytes[i] & ARRAY_LENGTH_0F];
            }
            fileName = new String(chars);
            return fileName;// 返回最后的uuid名称
        }
        catch (NoSuchAlgorithmException nsae)
        {
            throw nsae;
        }
    }
    
    /**
     * 获得对象的uuid和元数据存放位置格式
     * 
     * @return return value
     * @throws Exception Exception
     */
    public static Map<String, String> getUuid()
        throws Exception
    {
        return buildNodeFolderPath();
    }
}
