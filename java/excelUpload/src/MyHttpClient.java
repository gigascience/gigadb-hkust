import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sun.net.ftp.FtpClient;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-4-24
 */
public class MyHttpClient {
	static HttpClient client;
	String prefixFilePath = "configuration/prefix2url.xml";
	String errorInfoPath = "configuration/accessErrorInfo.xml";
	HashMap<String, String> prefixMap;
	// contain the error message when the webpage couldn't be accessed.
	HashMap<String, String> errorInfoMap;

	/**
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * 
	 */
	public MyHttpClient() throws ParserConfigurationException, SAXException,
			IOException {
		// TODO Auto-generated constructor stub
		client = new HttpClient();
		prefixMap = new HashMap<String, String>();
		errorInfoMap = new HashMap<String, String>();
		readXmlFiles();
	}

	public String getWebPage(String url) throws HttpException, IOException {
		GetMethod getMethod = new GetMethod(url);
		int status = client.executeMethod(getMethod);
		String webpage = getMethod.getResponseBodyAsString();
		getMethod.releaseConnection();
		// System.out.println("get web page");
		return webpage;
	}

	public String get_doi(String pmid) throws HttpException, IOException {
		String prefix = prefixMap.get("pmid");
		String postfix = "?term=" + pmid + "&presentation=xml";
		String url = HelpFunctions.getUrl(prefix, postfix);
		// String url = prefixMap.get("pmid") + "?term=" + pmid
		// + "&presentation=xml";
		String webpage = getWebPage(url);
		// System.out.println(webpage);
		String beginMark = "&lt;ArticleId IdType=\"doi\"&gt;";
		String endMark = "&lt;";
		int beginIndex = webpage.indexOf(beginMark) + beginMark.length();
		if (beginIndex < beginMark.length())
			return null;
		int endIndex = webpage.indexOf(endMark, beginIndex);
		String doi = webpage.substring(beginIndex, endIndex);
		return doi;
	}

	public String get_pmid(String doi) throws HttpException, IOException {
		String prefix = prefixMap.get("pmid");
		String postfix = "?term=" + doi + "&presentation=xml";
		String url = HelpFunctions.getUrl(prefix, postfix);
		String webpage = getWebPage(url);
		// System.out.println(webpage);
		String beginMark = "&lt;ArticleId IdType=\"pubmed\"&gt;";
		String endMark = "&lt;";
		int beginIndex = webpage.indexOf(beginMark) + beginMark.length();
		if (beginIndex < beginMark.length())
			return null;
		int endIndex = webpage.indexOf(endMark, beginIndex);
		String pmid = webpage.substring(beginIndex, endIndex).trim();
		if (pmid.equals(""))
			return null;
		return pmid;
	}

	// id doesn't contain the prefix "SAMPLE"
	public String get_sample_attributes(String id) throws HttpException,
			IOException {
		String prefix = prefixMap.get("SAMPLE");
		String postfix = id + "&format=text";
		String url = HelpFunctions.getUrl(prefix, postfix);
		String webpage = getWebPage(url);
		String[] lines = webpage.split("\n");
		String result = "";
		String comma = ",";
		boolean start = false;
		boolean descriptionTag = false;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.length() == 0)
				continue;
			if (line.equals("</pre>"))
				break;
			if (line.equals("Attributes:")
					|| line.equals("Additional attributes:")) {
				start = true;
				continue;
			} else if (line.equals("Description:")) {
				result += "description=\"";
				// read the next line
				result += (lines[i + 1].trim() + "\"");
				break;
			} else if (start) {
				if (line.charAt(0) == '/')
					line = line.substring(1);
				result += (line + comma);
			}
		}// for
		// remove the comma
		if (result.charAt(result.length() - 1) == ',')
			result = result.substring(0, result.length() - 1);
		return result;
	}

	// postfix should not be null or ""
	boolean accessTest(String postfix, String prefix, String field, String log)
			throws HttpException, IOException {
		if (postfix == null || postfix.equals("") || postfix.equals("null"))
			return true;
		// String logon_site
		// int LOGON_PORT=80;
		String url = HelpFunctions.getUrl(prefix, postfix);
		// if(ur)
		// System.out.println("**"+url+"** "+field);

		String urlRegex = Validation.urlRegex;
		if (!Validation.regexTest(urlRegex, url, field, log))
			return false;
		// client.getHostConfiguration().setHost(url, LOGON_PORT);
		try {
			GetMethod getMethod = new GetMethod(url);
			int status = client.executeMethod(getMethod);
			// not found the webpage
			if (status == 404) {
				Excel2Database.excel2DBLog.writeLine("ValidTest for " + field
						+ " : '" + postfix + "' cann't be resolved." + log);
				return false;
			} else if (status == 200) {
				if (errorInfoMap.keySet().contains(prefix)) {
					String pageContent = getMethod.getResponseBodyAsString();
					String errorInfo = errorInfoMap.get(prefix);
					if (pageContent.contains(errorInfo)) {
						Excel2Database.excel2DBLog.writeLine("ValidTest for "
								+ field + " : '" + postfix
								+ "' can't be resolved." + log);
						return false;
					}

				}
			}
			getMethod.releaseConnection();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(url);
		}
		return true;
	}

	HashSet<String> getCommon_name(String url, String[] nameArray)
			throws HttpException, IOException {
		String webpage = getWebPage(url);
		HashSet<String> nameSet = new HashSet<String>();
		String beginMark = "<em>Taxonomy ID: </em>";
		String endMark = "<em>Rank: </em>";
		int beginIndex = webpage.indexOf(beginMark) + beginMark.length();
		int endIndex = webpage.indexOf(endMark);

		String nameString = webpage.substring(beginIndex, endIndex);

		// get scientific name
		beginMark = "<title>Taxonomy browser (";
		endMark = ")</title>";
		beginIndex = webpage.indexOf(beginMark);
		if (beginIndex != -1) {
			beginIndex += beginMark.length();
			endIndex = webpage.indexOf(endMark, beginIndex);
			nameArray[0] = webpage.substring(beginIndex, endIndex);
		}
		else{
			Excel2Database.excel2DBLog.writeLine("Getting scientific name warning: "
					+ "can't get it!");
			nameArray[0]=null;
		}
		// get genbank common name
		beginMark = "<em>Genbank common name: </em><strong>";
		endMark = "</strong>";
		beginIndex = 0;
		beginIndex = nameString.indexOf(beginMark, beginIndex);
		if (beginIndex != -1) {
			beginIndex += beginMark.length();
			endIndex = nameString.indexOf(endMark, beginIndex);
			nameArray[1] = nameString.substring(beginIndex, endIndex);
		}
		else{
			Excel2Database.excel2DBLog.writeLine("Getting genbank common name warning: "
					+ "can't get it!");
			nameArray[1]=null;
		}
		beginMark = "<strong>";
		endMark = "</strong>";
		beginIndex = 0;
		while (true) {
			beginIndex = nameString.indexOf(beginMark, beginIndex);
			if (beginIndex == -1)
				break;
			beginIndex += beginMark.length();
			endIndex = nameString.indexOf(endMark, beginIndex);
			String common_name = nameString.substring(beginIndex, endIndex);
			// we use lower case
			nameSet.add(common_name.toLowerCase());
			beginIndex = endIndex + endMark.length();

		}
		if (nameSet.size() == 0) {
			Excel2Database.excel2DBLog.writeLine("Getting common name error: "
					+ "can't get the common name");
			return null;
		}
		return nameSet;
	}

	String getGenbank_name(String url) throws HttpException, IOException {
		String webpage = getWebPage(url);
		HashSet<String> nameSet = new HashSet<String>();
		String beginMark = "<em>Taxonomy ID: </em>";
		String endMark = "<em>Rank: </em>";
		int beginIndex = webpage.indexOf(beginMark) + beginMark.length();
		int endIndex = webpage.indexOf(endMark);
		String nameString = webpage.substring(beginIndex, endIndex);
		beginMark = "<em>Genbank common name: </em><strong>";
		endMark = "</strong>";
		beginIndex = 0;

		beginIndex = nameString.indexOf(beginMark, beginIndex);
		if (beginIndex == -1)
			return null;
		beginIndex += beginMark.length();
		endIndex = nameString.indexOf(endMark, beginIndex);
		String common_name = nameString.substring(beginIndex, endIndex);
		return common_name;
	}

	// read xml File
	void readXmlFiles() throws ParserConfigurationException, SAXException,
			IOException {
		// prefix2url.xml
		Document document = HelpFunctions.getDocument(prefixFilePath);
		Element element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (!(node instanceof Element))
				continue;
			String prefix = node.getNodeName();
			String url = node.getTextContent();
			prefixMap.put(prefix, url);
		}
		// accessError.xml
		document = HelpFunctions.getDocument(errorInfoPath);
		element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (node instanceof Element) {
				NodeList childList2 = node.getChildNodes();
				String url = null;
				String errorInfo = null;
				for (int j = 0; j < childList2.getLength(); j++) {
					Node node2 = childList2.item(j);
					if (node2 instanceof Element) {
						if (url == null)
							url = node2.getTextContent();
						else {
							errorInfo = node2.getTextContent();
							break;
						}
					}
				}
				errorInfoMap.put(url, errorInfo);
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, SQLException {
		Setting.Loadsetting();
		String doi = "10.1126/science.1211177";
		MyHttpClient myHttpClient = new MyHttpClient();
		// System.out.println(myHttpClient.get_pmid("10.1101/gr.121392.111"));
		// String pmid = "21940856";
		// System.out.println(myHttpClient.get_doi(pmid));
		// System.out.println(myHttpClient.get_sample_attributes("SRS173976"));
		String url = "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=90";
		System.out.println("*" + myHttpClient.getGenbank_name(url) + "*");
	}
}
