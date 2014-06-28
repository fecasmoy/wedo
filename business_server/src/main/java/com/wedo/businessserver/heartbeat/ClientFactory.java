package com.wedo.businessserver.heartbeat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 组播发出公共类
 * 
 * @author c90003207
 * 
 */
public class ClientFactory
{
    
    /**
     * 组播主体方法，发出组播
     * 
     * @param port port
     * @param msg msg
     * @throws Exception Exception
     */
    public static void run(int port, String msg)
        throws Exception
    {
        MulticastSocket multicastSocket = null;
        try
        {
            multicastSocket = new MulticastSocket();// 其实这里使用DatagramSocket发送packet就行
            // 绑定发送网卡
            multicastSocket.setInterface(InetAddress
                .getByName(com.wedo.businessserver.storage.jcr.Constants.INNERIP));
            // 设置组播地址
            InetAddress group = InetAddress.getByName("239.1.1.1");
            byte[] data = msg.getBytes();
            // 设置端口
            DatagramPacket packet =
                new DatagramPacket(data, data.length, group, port);
            multicastSocket.send(packet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (multicastSocket != null)
            {
                multicastSocket.close();
            }
        }
    }
}
