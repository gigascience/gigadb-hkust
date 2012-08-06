import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 */

/**
 * @author 王森洪
 *
 * @date 2012-4-18
 */
public class Extension2Type {
	Document document;
	String xmlFilePath = "configuration/format-extension.xml";
	String identifier;
	public HashMap<String, String> map;
	Extension2Type() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		File xmlFile = new File(xmlFilePath);
		document = db.parse(xmlFile);
		map=new HashMap<String, String>();
		read();
	}

	void read() {
		Element element = document.getDocumentElement();
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			NodeList childList2 = node.getChildNodes();
			String type=node.getNodeName();
			for (int j = 0; j < childList2.getLength(); j++) {
				Node node2 = childList2.item(j);
				if (node2 instanceof Element) {
					String extension=node2.getTextContent();
					map.put(extension,type);
//					values.add(node2.getTextContent());
//				System.out.println("扩展名:" + extension + "类型"+type);
//							+ node2.getTextContent());
				}
			}//for
			
		}
	}
	public static void main(String[] args) throws Exception {
		Extension2Type extension2Type=new Extension2Type();
//		validation.read();
	}
}
