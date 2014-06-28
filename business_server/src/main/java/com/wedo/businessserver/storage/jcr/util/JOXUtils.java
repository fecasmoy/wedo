package com.wedo.businessserver.storage.jcr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.wutka.jox.JOXBeanInputStream;
import com.wutka.jox.JOXBeanOutputStream;

/**
 * pojo与Xml序列化工具类
 * 
 * @author c90003207
 * 
 */
public class JOXUtils
{
    
    /**
     * 字符串序列换成对象
     * 
     * @param xml xml
     * @param className className
     * @return return value
     */
    public static Object fromXML(String xml, Class<?> className)
    {
        ByteArrayInputStream xmlData = new ByteArrayInputStream(xml.getBytes());
        JOXBeanInputStream joxIn = new JOXBeanInputStream(xmlData);
        try
        {
            return (Object) joxIn.readObject(className);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                xmlData.close();
                joxIn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 根据文件和对象通过反射进行转换
     * 
     * @param file file
     * @param className className
     * @return return value
     */
    public static Object fromXML(File file, Class<?> className)
    {
        FileInputStream xmlData = null;
        JOXBeanInputStream joxIn = null;
        try
        {
            xmlData = new FileInputStream(file);// 获得文件
            joxIn = new JOXBeanInputStream(xmlData);
            return (Object) joxIn.readObject(className);// 文件序列化成对象
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (xmlData != null)
                {
                    xmlData.close();
                }
                if (joxIn != null)
                {
                    joxIn.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 把对象按照编码格式转化为xml格式的字符串
     * 
     * @param bean 对象
     * @param encode 编码
     * @return return value
     */
    public static String toXML(Object bean, String encode)
    {
        ByteArrayOutputStream xmlData = new ByteArrayOutputStream();
        JOXBeanOutputStream joxOut = new JOXBeanOutputStream(xmlData, encode);
        try
        {
            joxOut.writeObject(beanName(bean), bean);// 按照格式输出流里面
            return xmlData.toString();
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                xmlData.close();
                joxOut.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取bean的名称
     * 
     * @param bean bean
     * @return return value
     */
    private static String beanName(Object bean)
    {
        String fullClassName = bean.getClass().getName();
        String classNameTemp =
            fullClassName.substring(fullClassName.lastIndexOf(".") + 1,
                fullClassName.length());// 取出.后面的后缀名
        return classNameTemp.substring(0, 1) + classNameTemp.substring(1);
    }
    
}
