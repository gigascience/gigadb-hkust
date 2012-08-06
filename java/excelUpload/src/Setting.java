

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class stores all parameter values needed by following tasks.
 * 
 * @author hupmscy
 * 
 */

public class Setting {
	public static String databaseUrl;
	public static String databaseUserName;
	public static String databasePassword;
	public static String schemaFile;
	public static String dataDir;
	public static String uploadDir;
	public static boolean isload=false;
	private Setting() {
	}
	/**
	 * 
	 * @param settingLoc
	 *            Location of setting file
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException
	 */

	public static void Loadsetting() throws ParserConfigurationException, SAXException, IOException {
		readXmlFiles();	
		isload=true;
	}
	// read xml File
	static  void readXmlFiles() throws ParserConfigurationException, SAXException,
			IOException {
		// validation.xml
		String settingLoc ="configuration/setting.xml";
		Document document=HelpFunctions.getDocument(settingLoc);
		Element element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (!(node instanceof Element))
				continue;
			String name=node.getNodeName();
			if(name.equals("databaseUrl"))
				databaseUrl=node.getTextContent();
			else if(name.equals("databaseUserName"))
				databaseUserName=node.getTextContent();
			else if(name.equals("databasePassword"))
				databasePassword=node.getTextContent();
			else if(name.equals("schemaFile"))
				schemaFile=node.getTextContent();
			else if(name.equals("excelDir"))
				dataDir=node.getTextContent();
			else if(name.equals("uploadDir"))
				uploadDir=node.getTextContent();
		}
	}
}
