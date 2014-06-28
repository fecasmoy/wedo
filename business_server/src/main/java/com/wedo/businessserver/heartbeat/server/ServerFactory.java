package com.wedo.businessserver.heartbeat.server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.event.Publisher;

/**
 * 服务器线程工厂类
 * 
 * @author c90003207
 * 
 */
public abstract class ServerFactory
    extends Thread
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(ServerFactory.class);
    
    /**
     * 默认大小
     */
    private static final int ARRAY_SIZE_4096 = 4096;
    
    /**
     * 国际化处理
     */
    private static ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * 组播监听端口
     */
    private int port;
    
    /**
     * mSG_I18N
     * 
     * @return the mSG_I18N
     */
    public static ResourceBundle getMsg()
    {
        return msg;
    }
    
    /**
     * mSG_I18N
     * 
     * @param mSGI18N the mSG_I18N to set
     */
    public static void setMsg(ResourceBundle mSGI18N)
    {
        msg = mSGI18N;
    }
    
    /**
     * 发布消息者
     * 
     * @return return value
     */
    protected Publisher getPublisher()
    {
        return (Publisher) BaseStaticContextLoader.getApplicationContext()
            .getBean("publisher");
    }
    
    /**
     * 组播实例化方法
     * 
     * @author l00100468
     * @param className 类名
     * @param listenrport 监听端口
     * @return server factory
     * @throws Exception exception
     */
    public static ServerFactory getInstance(String className, int listenrport)
        throws Exception
    {
        ServerFactory serverFactory = null;
        try
        {
            Class<?> c = Class.forName(className);
            serverFactory = (ServerFactory) c.newInstance();
            serverFactory.setPort(listenrport);
            return serverFactory;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * run method
     */
    @Override
    public void run()
    {
        while (true)
        {
            receive(getPort());// 绑定端口接受数据
        }
    }
    
    /**
     * 具体的组播处理
     * 
     * @param date date
     * @throws Exception exception
     */
    public abstract void listener(String date)
        throws Exception;
    
    /**
     * 组播接收方法,负责接收并获取相关信息
     * 
     * @author l00100468
     * @param targetPort 组播监听端口
     */
    public void receive(int targetPort)
    {
        MulticastSocket multicastSocket = null;
        try
        {
            multicastSocket = new MulticastSocket(targetPort);
            String innerIP = com.wedo.businessserver.storage.jcr.Constants.INNERIP;
            multicastSocket.setInterface(InetAddress.getByName(innerIP));// 绑定接受网卡
            InetAddress group = InetAddress.getByName("239.1.1.1");
            multicastSocket.joinGroup(group);
            multicastSocket.setLoopbackMode(true);// true自己发送，自己不接受
            byte[] data = new byte[ARRAY_SIZE_4096]; // 未填满空间会被0填充;长度超出数组则超出数据被忽略
            DatagramPacket packet = new DatagramPacket(data, data.length);
            multicastSocket.receive(packet);// 接受数据
            String date = new String(data, 0, packet.getLength());// 去掉空格等多余的字符
            listener(date);
            multicastSocket.close();
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00041") + "   port : "
                + targetPort, e);// 组播接收错误
        }
        finally
        {
            if (multicastSocket != null)
            {
                multicastSocket.close();
            }
        }
    }
    
    /**
     * getter port
     * 
     * @return port
     */
    public int getPort()
    {
        return port;
    }
    
    /**
     * setter port
     * 
     * @param port port
     */
    public void setPort(int port)
    {
        this.port = port;
    }
}
