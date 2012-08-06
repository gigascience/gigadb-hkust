import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;

import javax.swing.text.StyledEditorKit.ForegroundAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
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
public class HelpFunctions {

	public static boolean  findInArray(String content, ArrayList<String> values) {
		for (int i = 0; i < values.size(); i++)
			if (content.equals(values.get(i)))
				return true;
		return false;
	}
	public static String datePostfix(){
		TimeZone defaultTimeZone=TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar calendar=Calendar.getInstance();
		DateFormat dateFormat=new SimpleDateFormat("@yyyyMMddHHmm");
		String postfix=dateFormat.format(calendar.getTime());
		TimeZone.setDefault(defaultTimeZone);
		return postfix;
	}
	
	public static String SettoString(HashSet<String> set){
		String result="[";
		Object[] array=set.toArray();
		for(int i=0;i<array.length;i++){
			String element=(String)array[i];
			if(i!=array.length-1)
				result+=(element.toString()+", ");
			else
				result+=(element);
		}
		result+="]";
		return result;
	}
	
//	public static String get
	public static String trim(String string){
		int beginIndex=0;
		for(int i=0;i<string.length();i++){
			char temp=string.charAt(i);
			if(Character.isWhitespace(temp)||temp=='\u00a0'||
					temp=='\u2007'|| temp=='\u202f'){
				beginIndex++;			
			}
			else
				break;
		}
		int endIndex=string.length();
		for(int i=string.length();i>0;i--){
			char temp=string.charAt(i-1);
			if(Character.isWhitespace(temp)||temp=='\u00a0'||
					temp=='\u2007'|| temp=='\u202f'){
				endIndex--;		
			}
			else
				break;
		}
		if(endIndex<=beginIndex)
			return "";
		return string.substring(beginIndex, endIndex);
	}
	public static ArrayList<ArrayList<String>> copyList(ArrayList<ArrayList<String>> src){
		ArrayList<ArrayList<String>> dst=new ArrayList<ArrayList<String>>();
		for(ArrayList<String> childlist:src){
			ArrayList<String> temp=new ArrayList<String>();
			for(String child:childlist){
				if(child==null)
					temp.add(null);
				else
					temp.add(new String(child));
			}
			dst.add(temp);
		}
		return dst;
	}
	public static String getUrl(String prefix,String postfix){
		//attention $ is a reserved char in regex
		if(prefix.equals(""))
				return postfix;
		return prefix.replaceAll("\\$", postfix);
	}
//	public static void main(String[] args){
//		HashSet<String> set=new HashSet<String>();
//		set.add("se");
//		set.add("asdfasd");
//		System.out.println(SettoString(set));
//		System.out.println(trim(" "));
//	}
	// get document of xml file
	public static Document getDocument(String path) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		File xmlFile = new File(path);
		return db.parse(xmlFile);
	}
	//get map from xml file
	public static HashMap<String, String> getMap(String path) throws ParserConfigurationException, SAXException, IOException{
		Document document=getDocument(path);
		HashMap<String, String> map=new HashMap<String, String>();
		Element element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (!(node instanceof Element))
				continue;
			String key = node.getNodeName();
			String value = node.getTextContent();
			map.put(key, value);
		}
		return map;
	}
	public static HashMap<String, String> getMap2(String path) throws ParserConfigurationException, SAXException, IOException{
		Document document=getDocument(path);
		HashMap<String, String> map=new HashMap<String, String>();
		Element element = document.getDocumentElement();
		// System.out.println("根元素为:" + element.getTagName());
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (!(node instanceof Element))
				continue;
			NodeList grandChildList=node.getChildNodes();
			String key=null;
			String value=null;
			for(int j=0;j<grandChildList.getLength();j++){
				node = grandChildList.item(j);
				if (!(node instanceof Element))
					continue;
				if(key==null)
				 key = node.getTextContent();
				else{
					value = node.getTextContent();
					map.put(key, value);
					break;
				}
			}
		}
		return map;
	}
//	// get document of String
//	public static Document getDocument(String content) throws ParserConfigurationException,
//			SAXException, IOException {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		File xmlFile = new File(path);
//		return db.parse(xmlFile);
//	}
	public static Cell getCell(Sheet sheet, int row, int col) {
		Row row2 = sheet.getRow(row);
		return row2.getCell(col);
	}
	public static String getCellString(Cell value){
		String content=null;
		if(value.getCellType()==Cell.CELL_TYPE_NUMERIC){
			double num=value.getNumericCellValue();
			content=Double.toString(num);
			if(content.endsWith(".0"))
				content=content.substring(0,content.length()-2);
		}
		else
			content = value.getStringCellValue();
		return HelpFunctions.trim(content);
	}
	public static String replaceItalics(HSSFRichTextString richTextString,Workbook workbook) {
		String result = "";
		String plainString = richTextString.getString();
		boolean begin = false;
		for (int i = 0; i < richTextString.length(); i++) {
			HSSFFont font= (HSSFFont) workbook.getFontAt(richTextString.getFontAtIndex(i));
			if (font.getItalic()) {
				if (!begin) {
					begin = true;
					result += "<em>";
				}
				result += plainString.charAt(i);
			} else {
			
				if (begin) {
					begin = false;
					result += "</em>";
				}
				result += plainString.charAt(i);
			}//else
		}//for
		if(begin){
			result += "</em>";
		}
		return result;
	}
	public static String replaceItalics(XSSFRichTextString richTextString) {
		String result = "";
		String plainString = richTextString.getString();
		boolean begin = false;
		for (int i = 0; i < richTextString.length(); i++) {
			XSSFFont font = richTextString.getFontAtIndex(i);
			if (font.getItalic()) {
				if (!begin) {
					begin = true;
					result += "<em>";
				}
				result += plainString.charAt(i);
			} else {
				
				if (begin) {
					begin = false;
					result += "</em>";
				}
				result += plainString.charAt(i);
			}//else
		}//for
		if(begin){
			result += "</em>";
		}
		return result;
	}
	// find attribute
	public static Cell findCell(Sheet sheet, String content) {
		// we just read the first column
		for (Row row : sheet) {
			Cell cell = row.getCell(0);
			if (content.equals(cell.getStringCellValue()))
				return cell;
		}

		return null;
	}
	public static String mergeList(ArrayList<String> list){
		String result="";
		for(String s:list){
			result+=s;
		}
		return result;
	}
	
	public static Table getTable(ResultSet resultSet,int size) throws SQLException{
		Table table=new Table();
		while(resultSet.next()){
			ArrayList<String> record=new ArrayList<String>();
			for(int i=1;i<=size;i++){
				record.add(resultSet.getString(i));
			}
			table.recordList.add(record);
		}
		return table;
	}
	
	public static boolean compareList(ArrayList<String> list1,ArrayList<String> list2){
		int size=list1.size();
		for(int i=0;i<size;i++){
			if(!list1.get(i).equals(list2.get(i)))
				return false;
		}
		return true;
	}
	public static String getUrlRegex() {
		return "^((https|http|ftp)://)?" // protocol is not necessary
				+ "(" + "([0-9]{1,3}\\.){3}[0-9]{1,3}" // ip format url is
														// allowed
				// 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "([0-9a-zA-Z_!~*'()-]+\\.)*" // domain- www.
				+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // second
																	// domain
				+ "[a-zA-Z]{2,6}" // first level domain- .com or .museum
				+ ")" + "(:[0-9]{1,4})?" // 端口- :80
				+ "(" + "(/?)" // a slash isn't required if there is no file
				// name
				+ "|" + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?" + ")" + "$";
	}
	 public static boolean moveFile(File srcFile, String destPath)
	 {
	        // Destination directory
		 	String newFilePath=destPath+"/"+srcFile.getName();
	        File newFile=new File(newFilePath);     
	        // Move file to new directory
	        boolean success = srcFile.renameTo(newFile);       
	        srcFile.delete();
	        return success;
	    }
	 
	public static void createDir(String path){
		File file=new File(path);
		if(!file.exists()){
			file.mkdir();
		}		
	}
	public static void main(String[] args) throws Exception  {
		// TODO Auto-generated method stub
		File file=new File("uploadDir/test.txt");
		moveFile(file, "dataDir");
	}
	
}
