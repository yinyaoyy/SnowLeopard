/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.ckfinder.connector.ConnectorServlet;
import com.ckfinder.connector.errors.ConnectorException;
import com.thinkgem.jeesite.common.ckfinder.FtpGetFilesCommand;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * CKFinderConnectorServlet
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConnectorServlet extends ConnectorServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, false);
		/*String command = request.getParameter("command");
		String type = request.getParameter("type");
		System.out.println("command=["+command+"];type=["+type+"]");
		if("GETFILES".equals(command.toUpperCase())) {
			Document doc = null;
			try {
				doc = createDocument();
				Element rootElement = doc.createElement("Connector");
				if ((type != null) && StringUtils.isNotBlank(type)) {
					rootElement.setAttribute("resourceType", type);
				}
				FtpGetFilesCommand.createFilesData(doc, rootElement);
				response.getOutputStream().write(getDocumentAsText(doc).getBytes("UTF-8"));
			} catch (ConnectorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			super.doGet(request, response);
		}*/
		super.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, true);
		super.doPost(request, response);
	}
	
	private void prepareGetResponse(final HttpServletRequest request,
			final HttpServletResponse response, final boolean post) throws ServletException {
		Principal principal = (Principal) UserUtils.getPrincipal();
		if (principal == null){
			return;
		}
		String command = request.getParameter("command");
		String type = request.getParameter("type");
		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)){
			String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
			if (startupPath!=null){
				String[] ss = startupPath.split(":");
				if (ss.length==2){
					String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
							+ principal + "/" + ss[0] + ss[1];
					FileUtils.createDirectory(FileUtils.path(realPath));
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type!=null){
			String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
			String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
					+ principal + "/" + type + (currentFolder != null ? currentFolder : "");
			FileUtils.createDirectory(FileUtils.path(realPath));
		}
	}

	/**
	 * 创建一个文档，用于存储文件数据信息(xml格式)
	 * @author 王鹏
	 * @version 2018-08-01 09:36:56
	 * @return
	 * @throws ConnectorException
	 */
	public Document createDocument()
			throws ConnectorException
	{
		Document document = null;
		try
		{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
			document.setXmlStandalone(true);
		}
		catch (Exception e)
		{
			throw new ConnectorException(104, e);
		}
		return document;
	}
	/**
	 * 将文档转为字符串
	 * @author 王鹏
	 * @version 2018-08-01 09:37:26
	 * @param doc
	 * @return
	 * @throws ConnectorException
	 */
	public String getDocumentAsText(Document doc)
			throws ConnectorException
	{
		try
		{
			StringWriter stw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(new DOMSource(doc), new StreamResult(stw));
			return stw.toString();
		}
		catch (Exception e)
		{
			throw new ConnectorException(104, e);
		}
	}
}
