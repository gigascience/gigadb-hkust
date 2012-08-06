import java.io.File;
import java.io.IOException;
import java.security.spec.ECField;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Log.Log;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-4-24
 */
public class PerformanceTest {
	static ArrayList<String> failFile = new ArrayList<String>();
	/**
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 */
	static String sqlDir = "sqlFiles";
	

	public static void main(String[] args) throws Exception {
		Setting.Loadsetting();
		File dataDir = new File(Setting.uploadDir);
		HelpFunctions.createDir(sqlDir);
		ArrayList<String> sqlList = new ArrayList<String>();
		Log timeFile = new Log("updateTime.txt", false);
		int i = 0;
		Validation validation = new Validation();
		for (File file : dataDir.listFiles()) {
			if (file.isFile() == false || !file.getName().contains(".xls")
				)
				continue;
			else {
				i++;
				System.out.println("**Begin: file " + i + " : "
						+ file.getName() + " in process...");
				Excel2Database.excel2DBLog.writeLine("**Begin: file " + i
						+ " : " + file.getName() + " in process...");

				Log logTemp = new Log(sqlDir + "/" + file.getName() + ".sql",
						false);
				Excel2Database excel = new Excel2Database(dataDir + "/"
						+ file.getName());
				// close the validation test
				if ( !excel.fillTable() ){
//						|| !validation.validTest(excel.schema)) {
					System.out.println("End error: " + file.getName());
					Excel2Database.excel2DBLog.writeLine("**End error: "
							+ file.getName());
					Excel2Database.excel2DBLog.writeLine();
					failFile.add(file.getName());
					continue;
				}
		
				// format the records
				excel.schema.produceFormatRecordList();
				//fill primary Value list
				excel.schema.fillPrimaryValueList();
				ArrayList<String> sqlTemp = null;
				String identifier = excel.schema.getTable("dataset").getValue(
						"identifier", 0);
				int fileCount=excel.schema.getTable("file").recordList.size();
				if (excel.database.exist("dataset", "identifier", identifier)){
					int count=30;
					long time0=0;
					long time1=0;
	
					long bytes = file.length();
				
					for(int j=0;j<count;j++){
						long beginTime0=System.currentTimeMillis();
						ArrayList<String> sqlTemp0=excel.schema.createUpdateStmt_version4();
						excel.database.execute("begin;");
						 excel.database.execute(sqlTemp0);	
							excel.database.execute("commit;");
						 Long endTime0=System.currentTimeMillis();
						time0 += (endTime0-beginTime0);
						
						 long beginTime1=System.currentTimeMillis();
						 ArrayList<String> sqlTemp1=excel.schema.naive_createUpdateStmt();
						 excel.database.execute("begin;");
						 excel.database.execute(sqlTemp1);	
						 excel.database.execute("commit;");
						 Long endTime1=System.currentTimeMillis();
						time1 += (endTime1-beginTime1);
//						time1 += excel.database.execute(sqlTemp1);	
//						time2+= excel.database.execute(sqlTemp2);
//						time3 += excel.database.execute(sqlTemp3);
					}
					System.out.println("use the delete SQL statements, Time: "+time0/count);		
					System.out.println("just delete and then insert, Time: "+time1/count);	
					timeFile.writeLine(file.getName()+" "+bytes+" "+fileCount+" "+time0+" "+time1);
				}
//				else {
//					sqlTemp = excel.createInsertStmt(false);
//				}
//				
//				try {
////					System.out.println("here");
//					excel.database.execute("begin;");
//					excel.database.execute(sqlTemp);
//					System.out.println("**End success: " + file.getName());
//					Excel2Database.excel2DBLog.writeLine("**End success: "
//							+ file.getName());
//				} catch (Exception e) {
//					// TODO: handle exception
////					System.out.println()
//					excel.database.execute("rollback;");
//					failFile.add(file.getName());
//					Excel2Database.excel2DBLog.writeLine(file.getName());
//					e.printStackTrace(Excel2Database.excel2DBLog.printWriter);
//					System.out.println("End error: " + file.getName());
//					Excel2Database.excel2DBLog.writeLine("**End error: "
//							+ file.getName());
//					e.printStackTrace();
//				}
//				excel.database.execute("commit;");
//				logTemp.write(sqlTemp);
			}
			// new line
			Excel2Database.excel2DBLog.writeLine();
			// test
			// break;
		}// for
		System.out.print(failFile.toString());
		Excel2Database.excel2DBLog
				.writeLine("The following files has some errors, thus they are not uploaded!");
		Excel2Database.excel2DBLog.writeLine(failFile.toString());
	}

}
