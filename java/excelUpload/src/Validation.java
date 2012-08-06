import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.mozilla.javascript.ast.NewExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sun.net.ftp.FtpClient;

import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.WebConversation;

/**
 * @author 王森洪
 * 
 * @date 2012-4-17
 */
public class Validation {

	Document prefixDocument;
	// contains the CVs and regex of some fiel
	String validationPath = "configuration/validation.xml";
	MyHttpClient myHttpClient;
	String identifier;
	// boolean isValid;
	FtpClient ftpClient;
	HashMap<String, ArrayList<String>> cvMap;

	static String urlRegex;
	Cookie cookie;
	WebConversation webConversation;
	WebClient webClient;

	Validation() throws ParserConfigurationException, SAXException, IOException {
		cvMap = new HashMap<String, ArrayList<String>>();
		myHttpClient = new MyHttpClient();
		// isValid = true;
		// ignore the cookie
		DefaultHttpParams.getDefaultParams().setParameter(
				"http.protocol.cookie-policy", CookiePolicy.IGNORE_COOKIES);
		// client = new HttpClient();
		// client.getParams().setContentCharset("utf-8");
		ftpClient = new FtpClient();
		// HttpUnit in web
		webConversation = new WebConversation();
		readXmlFiles();
		urlRegex = cvMap.get("url").get(0);
	}

	// read xml File
	void readXmlFiles() throws ParserConfigurationException, SAXException,
			IOException {
		// validation.xml
		Document document = HelpFunctions.getDocument(validationPath);
		Element element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			NodeList childList2 = node.getChildNodes();
			ArrayList<String> values = new ArrayList<String>();
			for (int j = 0; j < childList2.getLength(); j++) {
				Node node2 = childList2.item(j);
				if (node2 instanceof Element) {
					values.add(node2.getTextContent());
				}
			}// for
			cvMap.put(node.getNodeName(), values);
		}
	}

	public boolean validTest(Schema schema) throws IOException, SQLException {
		boolean result = true;
		Table table = null;
		String target = null;
		String log = "";
		String prefix = "";
		// dataset identifer
		table = schema.getTable("dataset");
		target = "identifier";
		String identifier = table.getValue(target, 0);
		String regex = cvMap.get(target).get(0);
		result &= regexTest(regex, identifier, target, log);
		// dataset publisher
		target = "publisher";
		String publisher = table.getValue(target, 0);
		result &= validTest(target, publisher, log);
		// dataset dataset_type
		target = "dataset_type";
		table = schema.getTable("datasettype");
		result &= validTest(table, target, log);
		// dataset ext_acc-mirror
		log = " prefix error";
		target = "ext_acc_mirror";
		table = schema.getTable("datasetext_acc_mirror");
		// just test the prefix
		result &= validTest(table, target, log);
		result &= ext_acc_Test(table, target, "");
		// dataset ext_acc_link
		target = "ext_acc_link";
		table = schema.getTable("datasetext_acc_link");
		result &= validTest(table, target, log);
		result &= ext_acc_Test(table, target, "");
		log = "";
		// submitter_email
		target = "submitter_email";
		table = schema.getTable("submitter");
		String submitter_email = table.getValue(target, 0);
		regex = cvMap.get(target).get(0);
		result &= regexTest(regex, submitter_email, target, log);
		// author_name
		target = "author_name";
		table = schema.getTable("author");
		regex = cvMap.get(target).get(0);
		result &= regexTest(table, target, regex, log);
		// project url
		target = "project_url";
		table = schema.getTable("project");
		regex = urlRegex;
		prefix = "";
		// regexTest(table, target, regex, log);
		result &= accessTest(table, target, prefix, log);
		// determine if the url is in the database or not
		result &= uniqueProjectNameTest(table);
		// sample id
		table = schema.getTable("sample");
		target = "sample_id";
		log = "";
		result &= sample_idTest(table, target, log);
		// link_url
		target = "link_url";
		table = schema.getTable("external_link");
		result &= accessTest(table, target, "", log);
		// file type we don't need to test it
		// target="file_type";
		// table=schema.getTable("file");
		// log= "in file ";
		// validTest(table, target,log,"file_name");
		// file_description
		target = "file_type";
		table = schema.getTable("file");
		result &= validTest(table, target, log, "file_name");
		// manuscript table test
		table = schema.getTable("manuscript");
		result &= manuscriptTest(table, log);
		// log = "";
		// table = schema.getTable("manuscript");
		// target = "manuscript_doi";
		// prefix = myHttpClient.prefixMap.get(target);
		// result &=accessTest(table, target, prefix, log);
		// // PMID
		// target = "pmid";
		// regex = cvMap.get(target).get(0);
		// prefix = myHttpClient.prefixMap.get(target);
		// result &=regexTest(table, target, regex, log);
		// result &= accessTest(table, target, prefix, log);
		// external_link url
		target = "link_url";
		table = schema.getTable("external_link");
		prefix = "";
		result &= accessTest(table, target, prefix, log);
		// image iamge_location to be done???
		// image image_url
		target = "image_url";
		table = schema.getTable("image");
		prefix = "";
		result &= accessTest(table, target, prefix, log);
		// ftp_site to be done yet
		target = "ftp_site";
		table = schema.getTable("dataset");
		String url = table.getValue(target, 0);
		String ftp_site = "climb.genomics.cn";
		result &= ftpTest(ftp_site, url, target, log);
		// accessTest(table, target,"", log);
		// tax_id && common_name
		table = schema.getTable("species");
		target = "tax_id";
		regex = "\\d+";
		result &= regexTest(table, target, regex, log);
		prefix = myHttpClient.prefixMap.get(target);
		result &= speciesTest(table, target, prefix, log);
		// file
		table = schema.getTable("file");
		target = "file_name";
		result &= containFiles(table, target);
		log="";
		// file file_size
		target = "file_size";
		result &= file_sizeTest(table, target,log);

		return result;

	}

	// we'll pull doi/pmid here
	public boolean manuscriptTest(Table table, String log)
			throws HttpException, IOException {
		// manuscript table test
		boolean result = true;
		int size = table.recordList.size();
		for (int i = 0; i < size; i++) {
			// ArrayList<E>
			String doi = table.getValue("manuscript_doi", i);
			String pmid = table.getValue("pmid", i);
			if (pmid == null) {
				String target = "manuscript_doi";
				String prefix = myHttpClient.prefixMap.get(target);
				String regex = cvMap.get(target).get(0);
				// test if it can be accessed
				if (regexTest(regex, doi, target, log)
						&& myHttpClient.accessTest(doi, prefix, target, log)) {
					pmid = myHttpClient.get_pmid(doi);
					if (pmid == null) {
						Excel2Database.excel2DBLog
								.writeLine("ValidTest for doi: '"
										+ doi
										+ "'. Warning: can't not pull the information of pmid from it");
					} else
						// add it to table
						table.setValue("pmid", pmid, i);
				} else {
					result = false;
					pmid = null;
				}
			} else {
				// PMID test
				String target = "pmid";
				String regex = cvMap.get(target).get(0);
				String prefix = myHttpClient.prefixMap.get(target);
				// if the pmid is valid
				if (regexTest(regex, pmid, target, log)
						&& myHttpClient.accessTest(pmid, prefix, target, log)) {
					doi = myHttpClient.get_doi(pmid);
					// note: for pmid, there maybe not a doi
					if (doi == null) {
						Excel2Database.excel2DBLog
								.writeLine("ValidTest for pmid: '"
										+ pmid
										+ "'. Warning: can't not pull the information of doi from it");
						// result=false;
					} else {
						table.setValue("manuscript", doi, i);
					}
				} else {
					result = false;
					doi = null;
				}
			}
		}
		return result;
	}

	public boolean speciesTest(Table table, String target, String prefix,
			String log) throws HttpException, IOException {
		boolean result = true;
		int size = table.recordList.size();
		for (int i = 0; i < size; i++) {
			String postfix = table.getValue(target, i);
			if (myHttpClient.accessTest(postfix, prefix, target, log))
				result = common_nameTest(table, i, prefix, log);
			else
				result = false;
		}
		// isValid &= result;
		return result;
	}

	boolean validTest(Table table, String target, String log, String log_field)
			throws IOException {
		int size = table.recordList.size();
		boolean result = true;
		for (int i = 0; i < size; i++) {
			String content = table.getValue(target, i);
			String field = table.getValue(log_field, i);
			if (!validTest(target, content, log + field))
				result = false;
		}
		// isValid &= result;
		return result;
	}

	boolean accessTest(Table table, String target, String prefix, String log)
			throws HttpException, IOException {
		int size = table.recordList.size();
		boolean result = true;
		for (int i = 0; i < size; i++) {
			String postfix = table.getValue(target, i);
			if (!myHttpClient.accessTest(postfix, prefix, target, log))
				result = false;
		}
		// isValid &= result;
		return result;
	}

	// test if the ext_acc can be accessed and test if the url is valid or not
	boolean ext_acc_Test(Table table, String target, String log)
			throws IOException {
		int size = table.recordList.size();
		boolean result = true;
		for (int i = 0; i < size; i++) {
			String url = null;
			String ext_acc = table.getValue(target, i);
			String regex = "\\w+:\\w+";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(ext_acc);
			if (matcher.find()) {
				String[] temp = ext_acc.split(":");
				String prefix = myHttpClient.prefixMap.get(temp[0].trim());
				String postfix = temp[1].trim();
				result = myHttpClient.accessTest(postfix, prefix, target,
						" Warning for '" + ext_acc + "'");
			} else {
				if (!regexTest(urlRegex, ext_acc, target, log)) {
					result = false;
					continue;
				}
				result = myHttpClient.accessTest(ext_acc, "", target, log);
			}
		}
		// isValid &= result;
		//ext_acc should be return true
		return true;
	}

	boolean common_nameTest(Table table, int i, String prefix, String log)
			throws HttpException, IOException {
		String common_name = table.getValue("common_name", i);
		String tax_id = table.getValue("tax_id", i);
		String url = HelpFunctions.getUrl(prefix, tax_id);
		String[] pullNameArray = new String[2];
		HashSet<String> nameSet = myHttpClient.getCommon_name(url,
				pullNameArray);
		// add genbank_name and scientific name
		table.recordList.get(i).set(table.getIndex("scientific_name"),
				pullNameArray[0]);
		table.recordList.get(i).set(table.getIndex("genbank_name"),
				pullNameArray[1]);
		// here
		if (nameSet == null) {
			return false;
		}
		if (nameSet.contains(common_name.toLowerCase()))
			return true;
		else {
			Excel2Database.excel2DBLog.writeLine("ValidTest for common_name: '"
					+ common_name + "'. Warning for tax_id: " + tax_id
					+ " , the right one should be in: "
					+ HelpFunctions.SettoString(nameSet));
			return true;
		}
	}

	boolean validTest(Table table, String target, String log)
			throws IOException {
		boolean result = true;
		int size = table.recordList.size();
		for (int i = 0; i < size; i++) {
			String content = table.getValue(target, i);
			if (target.equals("ext_acc_mirror")
					|| target.equals("ext_acc_link")) {
				// test if it is a url or not, we test only when it isn't a url
				if (!content.startsWith("http"))
					result &= validTest(target, content.split(":")[0].trim(),
							log);
			} else {
				result &= validTest(target, content, log);
			}
		}
		// isValid &= result;
		return result;
	}

	// see Controlled vocabulary test
	boolean validTest(String target, String content, String log)
			throws IOException {
		boolean result = true;
		if (content == null || content.equals("") || content.equals("null"))
			;
		else {
			ArrayList<String> values = cvMap.get(target);
			if (!HelpFunctions.findInArray(content, values)) {
				Excel2Database.excel2DBLog.writeLine("validTest for " + target
						+ " : '" + content + "' " + " error " + log);
				// excel2DBLog.writeLine(log);
				result = false;
			}
		}
		// isValid &= result;
		return result;
	}

	boolean regexTest(Table table, String target, String regex, String log)
			throws IOException {
		boolean result = true;
		int size = table.recordList.size();
		for (int i = 0; i < size; i++) {
			String content = table.getValue(target, i);
			result &= regexTest(regex, content, target, log);
		}
		// isValid &= result;
		return result;
	}

	boolean file_sizeTest(Table table, String target, String log)
			throws IOException {
		boolean result = true;
		int size = table.recordList.size();
		for (int i = 0; i < size; i++) {
			String content=table.getValue(target, i);
			if(content.equals("0")){
				result=false;
				String file_name=table.getValue("file_name", i);
				Excel2Database.excel2DBLog.writeLine("ValidTest for " + target
						+ " Error; it's a empty file!"+" file_name: "+ file_name + log);
			}
		}
		// isValid &= result;
		return result;
	}

	static boolean regexTest(String regex, String content, String field,
			String log) throws IOException {
		if (content == null || content.equals("null") || content.equals(""))
			return true;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (!matcher.find()) {
			Excel2Database.excel2DBLog.writeLine("ValidTest for " + field
					+ " : '" + content + "' " + "url error " + log);
			return false;
		}
		return true;
	}

	boolean ftpTest(String ftp_site, String url, String field, String log)
			throws IOException {
		boolean result = true;
		// String path = ftp_site + file_location;
		ftpClient.openServer(ftp_site);
		ftpClient.login("anonymous", "1631");
		// ftpClient.binary();;
		int beginIndex = 0;
		if (url.indexOf(ftp_site) != -1)
			beginIndex = url.indexOf(ftp_site) + ftp_site.length();
		String location = url.substring(beginIndex);
		try {
			ftpClient.cd(location);
		} catch (Exception e) {
			// TODO: handle exception
			ftpClient.closeServer();
			Excel2Database.excel2DBLog.writeLine("ValidTest for " + field
					+ " : '" + url + "' cann't be resolved." + log);

			result = false;
		}
		ftpClient.closeServer();
		// isValid &= result;
		return result;
	}

	boolean uniqueProjectNameTest(Table table) throws SQLException, IOException {
		int size = table.recordList.size();
		// String field="project_url";
		boolean result = true;
		for (int i = 0; i < size; i++) {
			String url = table.getValue("project_url", i);
			String pattern = null;
			if (url.endsWith("/"))
				pattern = url.substring(0, url.length() - 1) + "%";
			else
				pattern = url + "%";
			String name = table.getValue("project_name", i);
			String query = "select project_name from project "
					+ "where project_url='" + pattern + "'";
			ResultSet resultSet = Excel2Database.database.stmt
					.executeQuery(query);
			if (resultSet.next()) {
				String oldName = resultSet.getString(1);
				if (!name.equals(oldName)) {
					result = false;
					Excel2Database.excel2DBLog.writeLine("ValidTest for "
							+ "project_name : '" + name
							+ "' doesn't equal to the " + " '" + oldName
							+ "' in database for project_url: " + url);
				}
			}

		}
		// isValid &= result;
		return result;
	}

	public boolean sample_idTest(Table table, String target, String log)
			throws HttpException, IOException {
		int size = table.recordList.size();
		boolean result = true;
		String prefix = myHttpClient.prefixMap.get("SAMPLE");
		for (int i = 0; i < size; i++) {
			String url = null;
			String sample_id = table.getValue(target, i);
			String regex = "SAMPLE:(\\w+)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(sample_id);
			if (matcher.find()) {
				int index_sample_attributes = table
						.getIndex("sample_attributes");
				if (!myHttpClient.accessTest(matcher.group(1), prefix, target,
						log)) {
					result = false;
				} else {
					// pull information of sample_attributes
					String sample_attributes = myHttpClient
							.get_sample_attributes(matcher.group(1));
					if (sample_attributes.length() != 0) {
						table.recordList.get(i).set(index_sample_attributes,
								sample_attributes);
					}
				}
			}
		}
		// isValid &= result;
		return result;
	}

	void printPage(String urls) throws HttpException, IOException, SAXException {
		// WebRequest req = new GetMethodWebRequest(url );
		// WebResponse resp = webConversation.getResponse( req );
		// String page=resp.getDOM().getTextContent();
		// System.out.println(page);

		UserAgentContext uacontext = new SimpleUserAgentContext();

		DocumentBuilderImpl builder = new DocumentBuilderImpl(uacontext);

		URL url = new URL(urls);

		InputStream in = url.openConnection().getInputStream();
		char[] buffer = new char[1000];
		try {

			Reader reader = new InputStreamReader(in, "utf-8");
			// int length=reader.read(buffer);
			// System.out.println(new String(buffer));
			InputSourceImpl inputSource = new InputSourceImpl(reader, urls);

			Document d = builder.parse(inputSource);
			System.out.print(d.getTextContent());

		} finally {

			in.close();

		}
		// not found the webpage
	}

	public boolean containFiles(Table table, String field) throws IOException {
		ArrayList<String> fileList = cvMap.get("mandatory_file");
		for (String target : fileList) {
			boolean found = false;
			for (int i = 0; i < table.recordList.size(); i++) {
				String filename = table.getValue("file_name", i);
				if (filename.equals(target)) {
					found = true;
					break;
				}
			}// for
			if (!found) {
				Excel2Database.excel2DBLog.writeLine("ValidTest for "
						+ "necessary file : '" + target
						+ "' Warning: doesn't exist.");
			}

		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		Validation validation = new Validation();
		// target="ftp_site";
		// table=schema.getTable("dataset");
		// accessTest(table, target,"", log);
		// validation.accessTest(" http://cucumber.genomics.org.cn","","");
		// System.out.println(validation.ftpTest("climb.genomics.cn",
		// "ftp://climb.geno mics.cn/pub/10.5524/",""));
		// System.out.println(validation.urlRegex);
		// System.out.println(validation.regexTest(validation.urlRegex,
		// "http://www.baidu.com/", ""));
		// 去除全角空格
		// temp=temp.replace((char)12288,' ');
		// //去除 non-break space
		// temp=temp.replace("\u00A0"," ");
		// validation.myTest();
		// validation.printPage("http://www.ebi.ac.uk/ena/data/view/");
		// System.out
		// .println(validation
		// .getCommon_name("http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=9557"));
		// System.out.println((int)
		// "?http://cucumber.genomics.org.cn".charAt(0));
	}

}
