/**
 * 
 */
package com.thinkgem.jeesite.common.ckfinder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.ckfinder.connector.data.ResourceType;
import com.ckfinder.connector.data.XmlAttribute;
import com.ckfinder.connector.data.XmlElementData;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.handlers.command.XMLCommand;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.ImageUtils;
import com.ckfinder.connector.utils.NaturalOrderComparator;
import com.google.common.collect.Lists;

/**
 * @author 王鹏
 * @version 2018-07-31 15:19:06
 */
public class FtpGetFilesCommand {

	public static void createFilesData(Document doc, Element rootElement)
	{
		Element element = doc.createElement("Files");
		String fullCurrentPath = "D:/temp/test";
		List<String> files = Arrays.asList("test1","test2","test3","test4");
		for (String filePath : files)
		{
			File file = new File(fullCurrentPath, filePath);
			if (file.exists())
			{
				XmlElementData elementData = new XmlElementData("File");
				XmlAttribute attribute = new XmlAttribute("name", filePath);
				elementData.getAttributes().add(attribute);
				attribute = new XmlAttribute("date", FileUtils.parseLastModifDate(file));
				
				elementData.getAttributes().add(attribute);
				attribute = new XmlAttribute("size", "3");
				elementData.getAttributes().add(attribute);
				/*if ((ImageUtils.isImage(file)) && (false))
				{
					String attr = createThumbAttr(file);
					if (!attr.equals(""))
					{
						attribute = new XmlAttribute("thumb", attr);
						elementData.getAttributes().add(attribute);
					}
				}*/
				elementData.addToDocument(doc, element);
			}
		}
		rootElement.appendChild(element);
	}
	
	/*private String createThumbAttr(File file)
	{
		File thumbFile = new File(this.configuration.getThumbsPath() + File.separator + this.type + this.currentFolder, file.getName());
		if (thumbFile.exists()) {
			return file.getName();
		}
		if (isShowThumbs()) {
			return "?".concat(file.getName());
		}
		return "";
	}*/
	
	
	/**
	 * @author 王鹏
	 * @version 2018-07-31 15:19:07
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
