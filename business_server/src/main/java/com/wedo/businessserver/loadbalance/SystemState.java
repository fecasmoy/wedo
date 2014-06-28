package com.wedo.businessserver.loadbalance;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

/**
 * linux 下cpu 内存 磁盘 jvm的使用监控
 * 
 * @author c90003207
 * 
 */
public class SystemState
{
    /**
     * cpu rate
     */
    private static final int CPU_RATE_100 = 100;
    
    /**
     * rate
     */
    private static final double RATE_1024D = 1024D;
    
    /**
     * system state
     */
    public SystemState()
    {
        try
        {
            Properties props = System.getProperties();
            String osName = props.getProperty("os.name");
            String libpath = System.getProperty("java.library.path");
            String path = null;
            if (libpath == null || libpath.length() == 0)
            {
                throw new RuntimeException("java.library.path is null");
            }
            StringTokenizer st =
                new StringTokenizer(libpath, System
                    .getProperty("path.separator"));
            if (st.hasMoreElements())
            {
                path = st.nextToken();
                System.out.println("====" + path);
            }
            else
            {
                throw new RuntimeException("can not split library path:"
                    + libpath);
            }
            if (osName.indexOf("Windows") != -1)
            {
                File file = new File(path + "/" + "sigar-x86-winnt.dll");
                System.out.println("----" + file.exists());
                System.err.println(SystemState.class
                        .getClassLoader().getResource("").getPath()
                        + "dll/sigar-x86-winnt.dll");
                System.out.println("=====" + file.getAbsolutePath());
                
                if (!file.exists())
                {
                    FileUtils.copyFileToDirectory(new File(SystemState.class
                        .getClassLoader().getResource("").getPath()
                        + "dll/sigar-x86-winnt.dll"), new File(path));
                }
            }
            else
            {
                File file = new File(path + "/" + "libsigar-amd64-linux.so");
                if (!file.exists())
                {
                    FileUtils.copyFileToDirectory(new File(SystemState.class
                        .getClassLoader().getResource("").getPath()
                        + "dll/libsigar-amd64-linux.so"), new File(path));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * cpu监控
     * 
     * @return double
     * @throws Exception Exception
     */
    public Double getCpuUsage()
        throws Exception
    {
        Sigar sigar = null;
        Double cpuUsage = Double.valueOf(0.0);
        try
        {
            sigar = new Sigar();
            CpuPerc[] infos = sigar.getCpuPercList();
            for (int i = 0; i < infos.length; i++)
            {
                CpuPerc info = infos[i];
                cpuUsage += info.getIdle();
            }
            cpuUsage = cpuUsage / infos.length;
            return cpuUsage * CPU_RATE_100;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
        finally
        {
            if (sigar != null)
            {
                sigar.close();
            }
        }
        return cpuUsage;
    }
    
    /**
     * 内存监控
     * 
     * @return value
     * @throws Exception Exception
     */
    public Double getMemUsage()
        throws Exception
    {
        Sigar sigar = null;
        try
        {
            sigar = new Sigar();
            Mem mem = sigar.getMem();
            return mem.getFree() / RATE_1024D / RATE_1024D;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (sigar != null)
            {
                sigar.close();
            }
        }
        return 0.0;
    }
    
    public static void main(String[] args) {
    	SystemState state = new SystemState();
		try {
			System.out.println("=======" + state.getCpuUsage() + "-----------" + state.getMemUsage());
		} catch (Exception e) {
			System.err.println("something error happens");
			e.printStackTrace();
		}
	}
}
