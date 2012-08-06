import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
public class Main {
	static ArrayList<String> failFile = new ArrayList<String>();
	/**
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 */
	static String sqlDir = "sqlFiles";
	static IntegrityCheck integrityCheck = new IntegrityCheck("MD5");

	public static int processExcel2Database(Validation validation,
			File xlsfile, int i) throws Exception {

		System.out.println("**Begin: file " + i + " : " + xlsfile.getName()
				+ " in process...");
		Excel2Database.excel2DBLog.writeLine("**Begin: file " + i + " : "
				+ xlsfile.getName() + " in process...");
		// store the sql statements
		Log logTemp = new Log(sqlDir + "/" + xlsfile.getName() + ".sql", false);
		Excel2Database excel = new Excel2Database(xlsfile.getAbsolutePath());
		// the following commands may change the databased
		String begin = "begin;";
		excel.database.execute(begin);
		int operation=0;
		try {
			if (excel.isReserved()) {
				System.out.println("End error: " + xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine("**End error: "
						+ xlsfile.getName()
						+ " is reserved! You are't allowed to upload it!");
				Excel2Database.excel2DBLog.writeLine();
				failFile.add(xlsfile.getName());
				return 0;
			}
			operation = integrityCheck.duplicate(xlsfile);
			if (operation == 1) {
				System.out.println("End error: " + xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine("**End error: "
						+ xlsfile.getName() + " duplicate upload!");
				Excel2Database.excel2DBLog.writeLine();
				failFile.add(xlsfile.getName());
				return operation;
			}
			if (!excel.fillTable() || !validation.validTest(excel.schema)) {
				System.out.println("End error: " + xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine("**End error: "
						+ xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine();
				failFile.add(xlsfile.getName());
				return 0;
			}
			// format the records
			excel.schema.produceFormatRecordList();
			// fill primary Value list
			excel.schema.fillPrimaryValueList();
			ArrayList<String> sqlTemp = null;
			// String identifier = excel.schema.getTable("dataset").getValue(
			// "identifier", 0);
			if (operation == 3)
				sqlTemp = excel.createupdateStmt();
			else if (operation == 2) {
				//false means we don't ignoreExist
				// if there are duplicate records, an exception will be throwed
				sqlTemp = excel.createInsertStmt(false);
			}
			try {
				excel.database.execute(sqlTemp);
				System.out.println("**End success: " + xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine("**End success: "
						+ xlsfile.getName());
			} catch (Exception e) {		
				Excel2Database.excel2DBLog.writeLine(xlsfile.getName());
				e.printStackTrace(Excel2Database.excel2DBLog.printWriter);
				System.out.println("End error: " + xlsfile.getName());
				Excel2Database.excel2DBLog.writeLine("**End error: "
						+ xlsfile.getName());
				e.printStackTrace();
				throw e;
			}
			logTemp.write(sqlTemp);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(excel.excel2DBLog.printWriter);
			excel.excel2DBLog.printWriter.flush();
			failFile.add(xlsfile.getName());
			String rollback = "rollback;";
			excel.database.execute(rollback);
			return 0;
		}
		excel.database.execute("commit;");
		return operation;
	}

	public static void processExcel(String filePath) throws Exception {
		File file = new File(filePath);
		Validation validation = new Validation();
		if (file.isDirectory()) {
			int i = 0;
			for (File xlsfile : file.listFiles()) {
				if (xlsfile.isFile() == false
						|| !xlsfile.getName().contains(".xls"))
					continue;
				else {
					i++;
					int operation=processExcel2Database(validation, xlsfile, i);
					// move files
					if (operation == 2 || operation == 3) {
						HelpFunctions.moveFile(xlsfile, Setting.dataDir);
					}
					else if(operation == 1){
						//remove the file
						xlsfile.delete();
					}
				}
			}// for
			System.out.print(failFile.toString());
			Excel2Database.excel2DBLog
					.writeLine("The following files has some errors, thus they are not uploaded!");
			Excel2Database.excel2DBLog.writeLine(failFile.toString());
		} else {
			if (file.isFile() == false || !file.getName().contains(".xls"))
				return;
			int operation=processExcel2Database(validation, file, 0);
			// move files
			if (operation == 2 || operation == 3) {
				HelpFunctions.moveFile(file, Setting.dataDir);
			}
			else if(operation == 1){
				//remove the file
				file.delete();
			}
		}
		}
	

	public static void main(String[] args) throws Exception {
		Setting.Loadsetting();
		if (args.length > 0) {
			System.out.println("get outside arguments!");
			System.out.println(args[0]);
			String filePath = args[0];
			processExcel(filePath);
			return;
		}
		processExcel(Setting.uploadDir);
	}

}
