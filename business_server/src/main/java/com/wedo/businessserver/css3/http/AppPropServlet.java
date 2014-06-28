package com.wedo.businessserver.css3.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.ws.model.WappProperties;
import com.wedo.businessserver.event.Publisher;
import com.wedo.businessserver.heartbeat.ClientFactory;
import com.wedo.businessserver.storage.jcr.Constants;

/**
 * 应用配置文件写入、更改servlet
 * 
 * @author l00100468
 */
public class AppPropServlet extends HttpServlet {

	/**
	 * 进行序列化的时候用到的序列码
	 */
	private static final long serialVersionUID = 3733642343668738243L;

	/**
	 * 记录日志的时候用到的日志工具类
	 */
	private Log logger = LogFactory.getLog(AppPropServlet.class);

	/**
	 * 国际化用到的工具类
	 */
	private ResourceBundle msg = LanguageUtil.getMessage();

	/**
	 * 响应HTTP的POST请求的方法实现
	 * 
	 * @param req
	 *            req
	 * @param resp
	 *            resp
	 * @throws ServletException
	 *             ServletException,IOException
	 * @throws IOException
	 *             IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String prop = req.getParameter("data");
		String appid = req.getParameter("appid");
		OutputStream out = null;
		try {
			if (StringUtils.isNotBlank(appid)) {
				logger.info(msg.getString("msg.app.addconfig") + " : " + appid);
				// 从配置文件中获取应用配置文件的地址
				// 一般在jcr路径下的AppProperties目录中
				String path = Constants.APP_PROPERTIES_ROOT;
				if (!new File(path).isDirectory()) {
					@SuppressWarnings("unused")
					Boolean flag = new File(path).mkdirs();
				}
				// 找到当前 应用的配置文件，如果该文件不存在时，需要创建
				String filepath = path + "/" + appid;
				File file = new File(filepath);
				if (file.exists()) {
					@SuppressWarnings("unused")
					Boolean flag = file.delete();
				}
				XStream sm = new XStream(new DomDriver());
				out = new FileOutputStream(file);
				// 将配置信息写入到应用配置文件中
				WappProperties newprop = (WappProperties) sm.fromXML(prop);
				sm.toXML(newprop, out);
				logger.info(msg.getString("msg.app.fileok"));

				// 更新或添加MAP关系
				Publisher pub = (Publisher) BaseStaticContextLoader
						.getApplicationContext().getBean("publisher");
				pub.publish(newprop);
				// 发送组播，通知到其他节点
				ClientFactory.run(
						com.wedo.businessserver.heartbeat.Constants.APP_PROPERTIES,
						appid);
			} else {
				// 无法创建应用配置文件，appid为空
				logger.error(msg.getString("ERROR.00302"));
				// TODO
			}
		} catch (Exception e) {
			// 创建应用配置文件失败
			logger.error(msg.getString("ERROR.00323"), e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return;
	}

	/**
	 * 相应HTTP的GET请求的方法实现
	 * 
	 * @param req
	 *            req
	 * @param resp
	 *            resp
	 * @throws ServletException
	 *             ServletException
	 * @throws IOException
	 *             IOException
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 直接调用POST方法进行响应
		doPost(req, resp);
	}

}
