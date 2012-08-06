import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import Log.Log;

import sun.print.resources.serviceui;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-4-29
 */
public class IntegrityCheck {
	static String tableName = "excelfile";
	String dataDir;
	// String cheSumRecFilePath = dataDir + "/MD5_Records.txt";
	// HashSet<String> md5Set;
	File file;
	private MessageDigest md = null;
	String md5 = null;
	long startTime;
	long endTime;
	Log timeFile = new Log("time.txt", false);

	/**
	 * @throws IOException
	 * 
	 */
	// ArrayList<>
	public IntegrityCheck(String arg) {
		// TODO Auto-generated constructor stub

		try {
			if (!Setting.isload) {
				Setting.Loadsetting();
			}
			dataDir = Setting.dataDir;
			md = MessageDigest.getInstance(arg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// 1: duplicate
	// 2: new file
	// 3. old file, update
	public int duplicate(File file) throws Exception {
//		System.out.println("bytes: " + file.length());
//		long bytes = file.length();
//		int count = 20;
//		long time = 0;
//
//		// time.w
//		startTime = System.nanoTime();
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));
		md.reset();
		int len = 0;
		byte[] buffer = new byte[8192];
		while ((len = bis.read(buffer)) > -1) {
			md.update(buffer, 0, len);
		}
		md5 = format(md.digest());
		if (Excel2Database.database.exist(tableName, "md5", md5)) {
//			endTime = System.nanoTime();
//			time += (endTime - startTime);
			return 1;
		}
		else{
			int result=processNewFile(file);
			return result;
		}
		// timeFile.writeLine(bytes + " " + time/count);
		// System.out.println("The execution time: " + time/count);
	
	}

	public void compareFile(File file, File oldFile) {

	}

	public int processNewFile(File file) throws Exception {

		int result = 2;
		Excel2Database excel = new Excel2Database(file.getAbsolutePath());
		String identifier = excel.getIdentifier();
		if (Excel2Database.database.exist(tableName, "identifier", identifier)) {
			String query = " select excelfile_name from excelfile where identifier= '"
					+ "' ;";
			ResultSet resultSet = Excel2Database.database.stmt
					.executeQuery(query);
			String oldFileName = null;
			if (resultSet.next()) {
				oldFileName = resultSet.getString(1);
			}
			// else{
			//				
			// }
			File oldFile = new File(dataDir + "/" + oldFileName);
			// compare the two files
			compareFile(file, oldFile);
			// delete the old ones
			String delete = " delete from excelfile where identifier='"
					+ identifier + "';";
			 Excel2Database.database.execute(delete);
			 oldFile.delete();
			result = 3;
		}
		String insert = " insert into " + tableName + " values( '" + identifier
				+ "' , '" + file.getName() + "' , '" + md5 + "' );";
		Excel2Database.database.execute(insert);
		//
//		endTime = System.currentTimeMillis();
//		long time = endTime - startTime;
//		System.out.println("execution time: " + time);	
		return result;
	}

	// public boolean exist(String path) throws Exception {
	// BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
	// path));
	// md.reset();
	// int len = 0;
	// byte[] buffer = new byte[8192];
	// while ((len = bis.read(buffer)) > -1) {
	// md.update(buffer, 0, len);
	// }
	// String s = format(md.digest());
	// if (md5Set.contains(s))
	// return true;
	// else {
	// Excel2Database excel = new Excel2Database(path);
	// String identifier = excel.getIdentifier();
	//
	// String record = s + " " + path + "\n";
	// // add it to file
	// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
	// new FileOutputStream(file, true)));
	// out.write(record);
	// out.flush();
	// return false;
	// }
	// }

	// public void removeOldExcelFile(String identifier,String path){
	// int beginIndex=path.lastIndexOf("/");
	// String fileName=path.substring(beginIndex);
	// String oldPath=dataDir+"/"+fileName;
	// File oldFile=new File(oldPath);
	// if(oldFile.exists()){
	// Excel2Database excel=new Excel2Database(oldPath);
	// String identifier_test=excel.getIdentifier();
	// if(identifier.equals(identifier_test)){
	// // close the excel file
	// excel.close();
	// oldFile.delete();
	// }
	// }
	// else{
	// //we need to
	// }
	//			
	// }
	public static String format(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int decValue;
		for (int i = 0; i < bytes.length; i++) {
			String hexVal = Integer.toHexString(bytes[i] & 0xFF);
			if (hexVal.length() == 1)
				hexVal = "0" + hexVal; // put a leading zero
			sb.append(hexVal);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Setting.Loadsetting();
		Excel2Database excel2Database = new Excel2Database("src/test.xls");
		IntegrityCheck integrityCheck = new IntegrityCheck("MD5");
		File dataDir = new File(Setting.dataDir);

		for (File file : dataDir.listFiles()) {
			if (integrityCheck.duplicate(file) == 1)
				System.out.println(file.getName() + " exist!");
			else
				System.out.println(file.getName() + " doesn't exist!");
		}
	}

}
