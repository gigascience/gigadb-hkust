import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;

import org.apache.commons.httpclient.HttpException;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.SAXException;

import sun.net.ftp.FtpClient;
import Log.Log;

/**
 * 
 */

/**
 * @author 王森洪 transfer data in excel files into dataset
 * @date 2012-3-31
 */
public class Excel2Database {
	Workbook workbook;
	Sheet studySheet;
	Sheet samplesSheet;
	Sheet filesSheet;
	Sheet linkSheet;
	Schema schema;
	Projects projects;
	String path;
	File file;
	boolean isValid;
	static Database database;
	// static Validation validation;
	static Extension2Type extension2Type;
	static String[] comExt = { "7z", "arj", "bz2", "bzip2", "cab", "cpio",
			"deb", "dmg", "gz", "gzip", "hfs", "iso", "lha", "lzh", "lzma",
			"rar", "rpm", "split", "swm", "tar", "taz", "tbz", "tbz2", "tgz",
			"tpz", "wim", "xar", "z", "zip" };

	static Log excel2DBLog = new Log("logFiles/" + "databaseLog"
			+ HelpFunctions.datePostfix() + ".txt", false);;

	static HashSet<String> compressExtension = new HashSet<String>(Arrays
			.asList(comExt));

	MyHttpClient myHttpClient;
	HashMap<String, String> locationMap;
	@SuppressWarnings("static-access")
	Excel2Database(String path) throws IOException,
			ParserConfigurationException, SAXException {
		if (!path.endsWith("xls") || !path.endsWith("xlsx"))
			MyFrame.textArea.setText(path + " is not an excel file");
		try {
			file = new File(path);
			// file doesn't exist
			if(!file.exists())
				return;
			this.path = path;
			// create an instance
			workbook = WorkbookFactory.create(file);
			// test
			// MyFrame.textArea.setText("asdfasdfasdfasdfadf"+workbook.getNumberOfSheets());
			filesSheet = workbook.getSheet("Files");
			samplesSheet = workbook.getSheet("Samples");
			studySheet = workbook.getSheet("Study");
			linkSheet = workbook.getSheet("Links");
			schema = new Schema();
			projects = new Projects();
			database = new Database();
			extension2Type = new Extension2Type();
			myHttpClient = new MyHttpClient();
			//
			// read xml file
		
			try{
				locationMap=HelpFunctions.getMap("configuration/doi-image.xml");
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace(excel2DBLog.printWriter);
			}
		} catch (Exception e) {
			// TODO: handle exception
			excel2DBLog.writeLine(e.toString());
			e.printStackTrace();
			MyFrame.textArea.setText(MyFrame.textArea.getText() + "\n" + path);
			MyFrame.textArea.setText(MyFrame.textArea.getText() + e.toString());
		}
	}
	//there is no close api in apachi poi
	public void close(){
//		
	}
	public void setInsertOrder(String table) {
		int num = schema.indexOf(table);
		schema.insertOrderList.add(num);
	}
	
	public boolean isReserved() throws SQLException, IOException{
		String identifier=getIdentifier();
		boolean result=database.exist("reserved_dataset", "identifier",identifier);
		if(result){
			excel2DBLog.writeLine("Valid Test for identifier: '"+identifier+
					"' Error: it is reserved. You can't upload it.");
		}
		else
			System.out.println(identifier+" is not reserved. You can use it.");
		return result;
	}
//	// get a filed's order in the database table 
//	public ArrayList<Integer> getIndexList(Table table,Sheet sheet){
//		ArrayList<Integer> indexList = new ArrayList<Integer>();
//		for (int col = 0; col < sheet.getColumns(); col++) {
//			Cell cell = sheet.getCell(col, 1);
//			// the release-data is data-stamp
//			String attribute = HelpFunctions.trim(cell.getContents());
//			if (attribute.equals("release_date"))
//				attribute = "date_stamp";
//			if (attribute.equals("file_path"))
//				attribute = "file_location";
//			if (attribute.length() == 0)
//				break;
//			int index = table.getIndex(attribute);
//			indexList.add(index);
//		}
//	}
	public void fillTable_file() throws IOException {
		setInsertOrder("file");
		Table table = schema.getTable("file");
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		// find the position of each cell in the attributeList
		Row topRow=filesSheet.getRow(1);
		
		int colNum=topRow.getLastCellNum();
		for (int col = 0; col < colNum; col++) {
			Cell cell = HelpFunctions.getCell(filesSheet,1, col);		
			// the release-data is data-stamp
			String attribute = HelpFunctions.trim(cell.getStringCellValue());
			if (attribute.equals("release_date"))
				attribute = "date_stamp";
			if (attribute.equals("file_path"))
				attribute = "file_location";
			if (attribute.length() == 0)
				break;
			int index = table.getIndex(attribute);
			indexList.add(index);
		}
		// read records
		for (int row = 2; row < filesSheet.getLastRowNum(); row++) {
			if (!next(filesSheet, row))
				break;
			// we need to set the size of record, it's the same with
			ArrayList<String> record = new ArrayList<String>();
			// init record
			initRecord(record, table.attributeList.size());
			for (int col = 0; col < indexList.size(); col++) {
				Cell cell = HelpFunctions.getCell(filesSheet, row, col);
				String attribute = table.attributeList.get(indexList.get(col));
				String content = HelpFunctions.trim(cell.getStringCellValue());
				// file_location file_path
				if (attribute.equals("file_location")
						&& !content.contains("ftp")) {
					String ftp_site = schema.getTable("dataset").getValue(
							"ftp_site", 0);
					if (content.startsWith("/") && ftp_site.endsWith("/"))
						content = content.substring(1);
					content = ftp_site + content;
					// remove the duplicate/
				}
				if (content.equals(""))
					record.set(indexList.get(col), null);
				else {
					record.set(indexList.get(col), content);
				}
			}
			// calculate some attributes. file_type file_size file_extension
			int index_path = table.getIndex("file_location");
			String file_path = record.get(index_path);
			// get file name from file_path
			String file_name = getFile_name(file_path);
			int index_name = table.getIndex("file_name");
			record.set(index_name, file_name);
			// get identifier
			String identifier = schema.getTable("dataset").getValue(
					"identifier", 0);
			int index_identifier = table.getIndex("identifier");
			record.set(index_identifier, identifier);
			String file_extension = getFile_extension(file_name);
			int index_extension = table.getIndex("file_extension");
			record.set(index_extension, file_extension);
			// calculate file_format
			int index_format = table.getIndex("file_format");
			String file_format = getFile_format(file_extension);
			record.set(index_format, file_format);
			// calculate file_size
			int index_location = table.getIndex("file_location");
			String file_location = record.get(index_location);
			long file_size = getFile_size(file_location);
			int index_size = table.getIndex("file_size");
			record.set(index_size, Long.toString(file_size));
			// add record to table
			table.recordList.add(record);
		}// for
	}

	public void fillTable_external_link() throws IOException {
		setInsertOrder("external_link");
		Table table = schema.getTable("external_link");
		// // add database
		// ArrayList<Integer> indexList = new ArrayList<Integer>();
		// // find the position of each cell in the attributeList
		// for (int col = 1; col < 3; col++) {
		// Cell cell = linkSheet.getCell(col, 1);
		// // the release-data is data-stamp
		// String attribute = cell.getContents().trim();
		// if (attribute.equalsIgnoreCase("prefix"))
		// attribute = "link_type";
		// if (attribute.equalsIgnoreCase("url"))
		// attribute = "link_url";
		// int index = table.getIndex(attribute);
		// indexList.add(index);
		// }
		// // read records
		// for (int row = 2; row < linkSheet.getRows(); row++) {
		// if (!next(linkSheet, row))
		// break;
		// // we need to set the size of record, it's the same with
		// ArrayList<String> record = new ArrayList<String>();
		// // init record
		// initRecord(record, table.attributeList.size());
		// for (int col = 1; col < 3; col++) {
		// Cell cell = linkSheet.getCell(col, row);
		// String content = cell.getContents().trim();
		// try {
		// record.set(indexList.get(col - 1), content);
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// }
		// // add record to table
		// table.recordList.add(record);
		// }// for
		// add additional_information
		String type = "additional_information";
		String content = getContent(studySheet, type);
		if (content.length() != 0) {
			String[] urlArray = content.split(",");
			for (int i = 0; i < urlArray.length; i++) {
				// we need to set the size of record, it's the same with
				ArrayList<String> record = new ArrayList<String>();
				// init record
				initRecord(record, table.attributeList.size());
				record.set(table.getIndex("link_type"), type);
				record.set(table.getIndex("link_url"), HelpFunctions
						.trim(urlArray[i]));
				table.recordList.add(record);
			}
		}
		// add genome_browser
		type = "genome_browser";
		content = getContent(studySheet, type);
		if (content.length() != 0) {
			String[] urlArray = content.split(",");
			for (int i = 0; i < urlArray.length; i++) {
				// we need to set the size of record, it's the same with
				ArrayList<String> record = new ArrayList<String>();
				// init record
				initRecord(record, table.attributeList.size());
				record.set(table.getIndex("link_type"), type);
				record.set(table.getIndex("link_url"), HelpFunctions
						.trim(urlArray[i]));
				table.recordList.add(record);
			}
		}
	}

	public void fillTable_sample() {
		setInsertOrder("sample");
		Table table = schema.getTable("sample");
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		// find the position of each cell in the attributeList
		for (int col = 0; col < 2; col++) {
			Cell cell = HelpFunctions.getCell(samplesSheet, 1, col);
				
			// the release-data is data-stamp
			String attribute = HelpFunctions.trim(cell.getStringCellValue());
			if (attribute.equals("species"))
				attribute = "tax_id";
			if (attribute.length() == 0)
				break;
			int index = table.getIndex(attribute);
			indexList.add(index);
		}
		for (int row = 2; row < samplesSheet.getLastRowNum(); row++) {
			//
			if (!next(samplesSheet, row))
				break;
			ArrayList<String> record = new ArrayList<String>();
			initRecord(record, table.attributeList.size());
			Cell cell = HelpFunctions.getCell(samplesSheet, row, 0);
				
			String sample_id = HelpFunctions.trim(cell.getStringCellValue());
			Cell cell2 = HelpFunctions.getCell(samplesSheet, row, 1);
			String tax_id = HelpFunctions.trim(HelpFunctions.getCellString(cell2));
			record.set(indexList.get(0), sample_id);
			record.set(indexList.get(1), tax_id);

			// record.add(content);
			table.recordList.add(record);
		}
		// find the position of each cell in the attributeList
	}

	public boolean next(Sheet sheet, int row) {
		Cell cell = HelpFunctions.getCell(sheet, row, 0);
		if (HelpFunctions.trim(cell.getStringCellValue()).length() == 0)
			return false;
		return true;
	}

	public void fillTable_species() {
		setInsertOrder("species");
		Table table = schema.getTable("species");
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		// find the position of each cell in the attributeList
		for (int col = 1; col < 3; col++) {
			Cell cell = HelpFunctions.getCell(samplesSheet, 1, col);
			
			// the release-data is data-stamp
			String attribute = HelpFunctions.trim(cell.getStringCellValue());
			if (attribute.equals("species"))
				attribute = "tax_id";
			else if (attribute.equals("species_common_name"))
				attribute = "common_name";
			if (attribute.length() == 0)
				break;
			int index = table.getIndex(attribute);
			indexList.add(index);
		}
		// read records
		// there maybe duplicate records here,so we need to add a judge here
		HashSet<String> previousRecords = new HashSet<String>();
		for (int row = 2; row < samplesSheet.getLastRowNum(); row++) {
			if (!next(samplesSheet, row))
				break;
			ArrayList<String> record = new ArrayList<String>();
			//init the size of the record
			initRecord(record, table.attributeList.size());
			boolean valid = true;
			for (int col = 1; col < 3; col++) {
				Cell cell = HelpFunctions.getCell(samplesSheet, row, col);
				
				String content = HelpFunctions.trim(HelpFunctions.getCellString(cell));
				if (col == 1 && previousRecords.contains(content)) {
					valid = false;
					break;
				} else {
					previousRecords.add(content);
				}
				try {
					// here is a trick -1
					record.set(indexList.get(col - 1), content);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			//
			if (valid)
				table.recordList.add(record);
		}
	}

	public void fillTable_image() {

		setInsertOrder("image");
		Table table = schema.getTable("image");
		int size = table.attributeList.size();
		ArrayList<String> record = new ArrayList<String>();
		String key="_"+getIdentifier().split("/")[1];
		for (int i = 0; i < size; i++) {
			//image_location from map
			String attribute=table.attributeList.get(i);
			if(attribute.equals("image_location")){
				record.add(locationMap.get(key));
				continue;
			}
			Cell cell = HelpFunctions.findCell(studySheet,attribute);
			// we can't find the attribute.
			if (cell == null) {
				//if there is not url, then there should not be a image
//				if(table.attributeList.get(i).equals("image_url"))
//					return;
				record.add(null);
				continue;
			}
			String content = getContent(studySheet, table.attributeList.get(i));
			if (content == null || content.equals(""))
				record.add(null);
			else
				record.add(content);
		}
		table.recordList.add(record);
	}

	public void fillTable_project() {
		setInsertOrder("project");
		Projects projects=null;
		try {
			projects = new Projects();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace(excel2DBLog.printWriter);
		}
		Table table = schema.getTable("project");
		int size = table.attributeList.size();
		String content = getContent(studySheet, "project_URL");
		if (content.equals(""))
			return;
		String[] project_urls = getProjectUrls(content);
		for (int i = 0; i < project_urls.length; i++) {
			ArrayList<String> record = new ArrayList<String>();
			String url = project_urls[i];
			HashMap<String, String> details = projects.getDetails(url);
			for (int j = 0; j < table.attributeList.size(); j++) {
				String attribute = table.attributeList.get(j);
				String value = details.get(attribute);
				// System.out.println("project: " + value);
				record.add(value);
			}
			table.recordList.add(record);
		}
	}
	
	String getIdentifier(){
		return getContent(studySheet, "identifier");
	}

	static String[] getProjectUrls(String content) {
		ArrayList<String> list = new ArrayList<String>();
		int count = 0;
		int beginIndex = 0;
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '"')
				count++;
			if (content.charAt(i) == ',') {
				if (count > 0 && count % 2 == 0) {
					String url = content.substring(beginIndex, i);
					list.add(HelpFunctions.trim(url));
					beginIndex = i + 1;
				}
			}
		}
		// don't forget the last one
		String url = content.substring(beginIndex);
		list.add(url);
		// System.out.println(list.toString());
		return list.toArray(new String[list.size()]);
	}

	public void fillTable_submitter() {
		setInsertOrder("submitter");
		Table table = schema.getTable("submitter");
		int size = table.attributeList.size();
		ArrayList<String> record = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			String content = getContent(studySheet, table.attributeList.get(i));
			// because in submitter there is not null attributes.
			if (content == null)
				record.add("");
			else
				record.add(content);
		}
		// we should add record first to avoid null pointer error
		table.recordList.add(record);
	}

	public void fillTable_manuscript() throws HttpException, IOException {
		setInsertOrder("manuscript");
		Table table = schema.getTable("manuscript");
		int size = table.attributeList.size();
		String content = getContent(studySheet, "related_manuscript");
		if (content == "")
			return;
		String[] valueList = content.split(",");
		for (int i = 0; i < valueList.length; i++) {
			ArrayList<String> record = new ArrayList<String>();
			initRecord(record, size);
			String value = HelpFunctions.trim(valueList[i]);
			String doi = null;
			String pmid = null;
			// a doi should contains /
			if (value.contains("/")) {
				// get pmid
				doi = value;
			} else {
				pmid = value;
			}
			// fill
			int index_doi = table.getIndex("manuscript_doi");
			int index_pmid = table.getIndex("pmid");
			record.set(index_doi, doi);
			record.set(index_pmid, pmid);
			table.recordList.add(record);
		}
		// test
		// table.print();
	}

	public void fillTable_dataset() throws IOException {
		setInsertOrder("dataset");
		Table table = schema.getTable("dataset");
		int size = table.attributeList.size();
		ArrayList<String> record = new ArrayList<String>();
		String key="_"+getIdentifier().split("/")[1];
		for (int i = 0; i < size; i++) {
			String attribute=table.attributeList.get(i);
			if(attribute.equals("image_location")){
				record.add(locationMap.get(key));
				continue;
			}
			String content = getContent(studySheet,attribute);
			// we can't find the attribute.
			// publication_date, modification_date
			if (content == null || content.equals("")) {
				record.add(null);
				continue;
			}
			if(attribute.equals("dataset_size")){
				double dataset_size=Double.parseDouble(content);
				System.out.println(dataset_size);
				dataset_size*=(1024*1024*1024);
//				dataset_size=(long)dataset_size;
				long long_size=new Double(dataset_size).longValue();
//				System.out.println(long_size);
				content=String.valueOf(long_size);
//				System.out.println(content);
			}
//			if(attribute.equals("description")){
//				System.out.println(content);
//				for(int l=0;l<content.length();l++){
//					System.out.println(content.charAt(l)+" : "+(int)(content.charAt(l)));
//				}
//			}
			
			record.add(content);
		}

		table.recordList.add(record);
		// test
		// table.print();
	}

	// String[] deleteDuplicate(String[] authors){
	//		
	// }
	public void fillTable_author() {
		setInsertOrder("author");
		Table table = schema.getTable("author");
		int size = table.attributeList.size();
		// ArrayList<String> record=new ArrayList<String>();
		// add author names
		String[] nameList = getContent(studySheet, "author_list").split(
				";| +and +");
		String identifier = schema.getTable("dataset")
				.getValue("identifier", 0);
		for (int i = 0; i < nameList.length; i++) {
			ArrayList<String> record = new ArrayList<String>();
			initRecord(record, size);
			String author_name = HelpFunctions.trim(nameList[i]);
			// add auto correct function
			author_name = author_name.replaceAll(" +", " ");
			int rank = i + 1;
			int index_name = table.getIndex("author_name");
			int index_rank = table.getIndex("rank");
			int index_identifer = table.getIndex("identifier");
			int index_orcid = table.getIndex("orcid");

			record.set(index_name, author_name);
			record.set(index_orcid, null);
			record.set(index_rank, Integer.toString(rank));
			record.set(index_identifer, identifier);
			table.recordList.add(record);
		}
		// test
		// table.print();
	}

	public void fillTable_multiAttribute(String attribute) throws IOException {

		String tableName = null;
		if (attribute.equals("dataset_type"))
			tableName = "datasettype";
		else
			tableName = "dataset" + attribute;
		setInsertOrder(tableName);
		Table table = schema.getTable(tableName);
		String identifier = schema.getTable("dataset")
				.getValue("identifier", 0);
		// attribute=attribute.toLowerCase();
		String content = getContent(studySheet, attribute);
		String[] attributeList = null;
		// size the number of records
		int size = 0;
		// blank
		if (content.length() != 0) {
			attributeList = content.split(",");
			size = attributeList.length;
		}
		int index_attribute = table.getIndex(attribute);
		int index_identifer = table.getIndex("identifier");
		for (int i = 0; i < size; i++) {
			ArrayList<String> record = new ArrayList<String>();
			initRecord(record, 2);
			String value = HelpFunctions.trim(attributeList[i]);
			record.set(index_attribute, value);
			record.set(index_identifer, identifier);
			table.recordList.add(record);
		}
		// test
		// table.print();
	}

	// relation
	public void fillTable_dataset_(String tableName2) {
		String tableName = "dataset_" + tableName2;
		setInsertOrder(tableName);
		Table table = schema.getTable(tableName);
		Table table2 = schema.getTable(tableName2);
		ArrayList<String> attributeList = table.attributeList;
		String attribute2 = null;
		for (String attribute : attributeList) {
			if (!attribute.equals("identifier")) {
				attribute2 = attribute;
				break;
			}
		}
		String identifier = schema.getTable("dataset")
				.getValue("identifier", 0);
		for (int i = 0; i < table2.recordList.size(); i++) {
			String value = table2.getValue(attribute2, i);
			ArrayList<String> record = new ArrayList<String>();
			initRecord(record, 2);
			int index_attribute = table.getIndex(attribute2);
			int index_identifer = table.getIndex("identifier");
			record.set(index_attribute, value);
			record.set(index_identifer, identifier);
			table.recordList.add(record);
		}

	}

	public  String getContent(Sheet sheet, String attribute) {
		Cell cell = HelpFunctions.findCell(sheet, attribute);
		// we can't find the attribute.
		if (cell == null) {
			return null;
		}
		int row = cell.getRowIndex();
		int col = cell.getColumnIndex();
		// calcualte the position of the value
		col += 2;
		Cell value = HelpFunctions.getCell(sheet, row, col);
		String content=null;
		// value.
		if(attribute.equals("title") || attribute.equals("description")){
			RichTextString richTextString=value.getRichStringCellValue();
			if(richTextString instanceof HSSFRichTextString){	
				content=HelpFunctions.replaceItalics((HSSFRichTextString) richTextString, workbook);
			}
			else{
				content=HelpFunctions.replaceItalics((XSSFRichTextString) richTextString);
			}
			System.out.println("italics found: "+content);
		}
		else{
			content=HelpFunctions.getCellString(value);
		}
		content = content.replaceAll("\\u00a0", " ");
		content = content.replaceAll("\\ufffd", " ");
		return content;
	}

	// 获取extension
	public String getFile_extension(String file_name) throws IOException {
		// file_name="asdfasd.txt";
		String[] extensionArray = file_name.split("\\.");
		String extension = "";
		int length = extensionArray.length;
		if(length == 1){
			excel2DBLog.writeLine("Getting file_extension, file_name: "
					+file_name+ " Warning there is not '.' in it!");				
			return "unknown";
		}
		//the first one shouldn't be extension
		for (int i = 1; i < length; i++) {
			String temp = HelpFunctions.trim(extensionArray[i]);
			// all extension are lower case in map, so when camparing,
			// I need to change temp to lowercase
			//if readme then the extension before it is removed
			if(temp.equals("readme")){
				extension="";
				continue;
			}
			if (extension2Type.map.keySet().contains(temp.toLowerCase())) {
				if (extension != "" && temp.equals("txt"))
					continue;
				extension = temp;
			}
		}
		if (extension == "") {
			int index = length - 1;
			while (compressExtension.contains(extensionArray[index]
					.toLowerCase()))
				index--;
			extension = extensionArray[index];
		}
		return extension;
	}

	public static String getFile_name(String file_path) {
		int beginIndex = file_path.lastIndexOf("/") + 1;
		return file_path.substring(beginIndex);
	}

	public String getFile_format(String file_extension) {
		//to lower case
		if (extension2Type.map.containsKey(file_extension.toLowerCase()))
			return extension2Type.map.get(file_extension.toLowerCase());
		else
			return "UNKNOWN";
	}

	// size the number of attributes in a record
	public void initRecord(ArrayList<String> record, int size) {
		for (int i = 0; i < size; i++)
			record.add(null);
	}

	public static long getFile_size(String file_location) throws IOException {
		// String
		// ftp_site=(String)schema.getTable("dataset").getAttribute("ftp_site");
		String ftp_site = "climb.genomics.cn";
		// String path = ftp_site + file_location;
		FtpClient ftpClient = new FtpClient();
		ftpClient.openServer(ftp_site);
		long fileSize = -1;
		// ftpClient.login("senhong", "senhong1631");
		ftpClient.login("anonymous", "senhong1631");
		// ftpClient.binary();;
		int beginIndex = 0;
		if (file_location.indexOf(ftp_site) != -1)
			beginIndex = file_location.indexOf(ftp_site) + ftp_site.length();
		String location = file_location.substring(beginIndex);
		String request = "SIZE " + location + "\r\n";
		ftpClient.sendServer(request);
		try {
			String temp = ftpClient.getResponseString();
			int status = ftpClient.readServerResponse();
			if (status == 213) {
				String msg = ftpClient.getResponseString();
				fileSize = Long.parseLong(msg.substring(3).trim());
			} else {
				excel2DBLog
						.writeLine("We can't get the file, please check its path: ");
				excel2DBLog.writeLine(file_location);
			}

		} catch (IOException e) {
			e.printStackTrace();
			excel2DBLog.writeLine(e.toString());
		}
		ftpClient.closeServer();
		return fileSize;
	}

	public boolean  fillTable() throws IOException {
		boolean result=true;
		try{
		fillTable_submitter();
		fillTable_image();
		fillTable_dataset();
		fillTable_author();
		fillTable_multiAttribute("ext_acc_mirror");
		fillTable_multiAttribute("ext_acc_link");
		fillTable_multiAttribute("dataset_type");
		fillTable_manuscript();
		fillTable_dataset_("manuscript");
		fillTable_species();
		fillTable_dataset_("species");
		fillTable_sample();
		fillTable_dataset_("sample");
		fillTable_file();
		// fillTable_dataset_("file");
		fillTable_external_link();
		fillTable_dataset_("external_link");
		fillTable_project();
		fillTable_dataset_("project");
		}catch (Exception e) {
			// TODO: handle exception
			// TODO: handle exception
//			Excel2Database.excel2DBLog.writeLine(file.getName());
			// excel2DBLog.writeLine("!!!!!!!!!!!!!!!!");
			// excel2DBLog.writeLine("!!!!!!!!!!!!!!!!");
			// e
//			Excel2Database.excel2DBLog.writeLine(e.toString());
//			PrintStream ps = new PrintStream(fos);  
//			PrintStream printStream=new PrintStream(out, autoFlush, encoding)		
			e.printStackTrace();
			e.printStackTrace(Excel2Database.excel2DBLog.printWriter);
			Excel2Database.excel2DBLog.printWriter.flush();
			result=false;
		}
		// format records
		return result;
	}

	public ArrayList<String> createupdateStmt() throws IOException,
			SQLException {
		
//		return schema.createUpdateStmt_version2();
//		return schema.createUpdateStmt();
		return schema.naive_createUpdateStmt();
		
	}

	public ArrayList<String> createInsertStmt(boolean ignoreExist) throws IOException {
		return schema.createInsertStmt(ignoreExist);
	}
	public ArrayList<String> naive_createUpdateStmt() throws IOException, SQLException {
		return schema.naive_createUpdateStmt();
	}
	public static void main(String[] args) throws Exception {
	}

}
