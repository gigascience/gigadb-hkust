import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import jxl.read.biff.Record;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-3-31
 */
public class Schema {
	ArrayList<Table> tableList;
	ArrayList<Integer> insertOrderList;
	Database database;

	/**
	 * @throws IOException
	 * 
	 */
	public Schema() throws IOException, Exception {
		// TODO Auto-generated constructor stub
		tableList = new ArrayList<Table>();
		insertOrderList = new ArrayList<Integer>();
		database = new Database();
		init();
	}

	public int indexOf(String tableName) {
		int i = 0;
		for (Table table : tableList) {
			if (table.name.equalsIgnoreCase(tableName)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public Table getTable(String name) {
		for (Table table : tableList) {
			if (table.name.equalsIgnoreCase(name))
				return table;
		}
		return null;
	}

	public void init() throws IOException {
		// Setting.Loadsetting();
		File file = new File(Setting.schemaFile);
		InputStream inputStream = new FileInputStream(file);
		// 改文件必须放于binary下面
		// InputStream
		// inputStream=this.getClass().getResourceAsStream("/gigascience.sql");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String line = reader.readLine();
		boolean begin = false;
		Table table = null;
		while (line != null) {
			// the end of the table
			if (line.trim().startsWith(")")) {
				begin = false;
				tableList.add(table);
			}
			// begin to read the attributes of the table
			if (begin) {
				String[] parts = line.trim().split(" +");
				String attribute = parts[0];
				String type = parts[1];
				if (parts[1].endsWith(","))
					type = type.substring(0, type.length() - 1);
				table.attributeList.add(attribute);
				table.typeList.add(type);
			}
			if (line.contains("CREATE TABLE")) {
				begin = true;
				table = new Table();
				String[] parts = line.split(" +");
				table.name = parts[2];
			}
			// extract primary attributes
			if (line.contains("ALTER TABLE ONLY")) {
				String nextLine = reader.readLine();
				if (nextLine.contains("PRIMARY KEY")) {
					String[] temp = line.split(" +");
					// the last part
					String tableName = temp[temp.length - 1].trim();
					int beginIndex = nextLine.indexOf("(") + 1;
					int endIndex = nextLine.indexOf(")");
					String primaryKey = nextLine
							.substring(beginIndex, endIndex).trim();
					temp = primaryKey.split(",");
					for (int j = 0; j < temp.length; j++)
						this.getTable(tableName).primaryAttributeList
								.add(temp[j].trim());
				}
			}
			line = reader.readLine();
		}
	}

	public ArrayList<String> createInsertStmt(boolean ignoreExist) {
		ArrayList<String> stmtList = new ArrayList<String>();
		// stmt+=("begin;\n");
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("file") || table.equals("author")
					|| tableName.contains("dataset"))
				stmtList.addAll(table.createInsertStmt(ignoreExist));
			else {
				stmtList.addAll(table.createInsertStmt(true));
			}
		}
		// stmt+=("commit;\n");
		return stmtList;
	}

	public void fillPrimaryValueList() {
		for (int i = 0; i < tableList.size(); i++) {
			Table table = tableList.get(i);
			table.fillPrimaryValueList();
		}
	}

	public ArrayList<String> createUpdateStmt() throws SQLException,
			IOException {
		ArrayList<String> stmtList = new ArrayList<String>();
		String identifier = getTable("dataset").getValue("identifier", 0);
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("dataset")) {
				stmtList.addAll(table.createUpdateStmt());
			}
			// delete or insert
			else if (tableName.equals("file") || tableName.equals("author")) {
				String addColumn = "alter table " + tableName + " add column "
						+ " mark_sh boolean;";
				stmtList.add(addColumn);
				// Excel2Database.database.execute(addColumn);
				for (int j = 0; j < table.recordList.size(); j++) {
					// add records that are not in database
					if (!Excel2Database.database.exist(table, j))
						stmtList.add(table.createInsertStmt(j, false));
					else {
						// update the record, because some attributes other than
						// primary key
						// may be changed
						stmtList.add(table.createUpdateStmt(j));
						// add mark
						String setMark = "update " + tableName
								+ " set mark_sh = true "
								+ table.updateWhereClause(j);
						stmtList.add(setMark);
					}
				}// for
				// delete the records whihc are not in the new records
				String deleteRecord = "delete from " + tableName
						+ " where mark_sh is null " + "and identifier='"
						+ identifier + "';";
				stmtList.add(deleteRecord);
				// drop the mark_sh column
				String deleteMark = "alter table " + tableName
						+ " drop column mark_sh;";
				stmtList.add(deleteMark);
			}
			// only have two attributes and they are all primay key
			else if (tableName.contains("dataset")) {
				String addColumn = "alter table " + tableName + " add column "
						+ " mark_sh boolean;";
				stmtList.add(addColumn);
				// Excel2Database.database.execute(addColumn);
				for (int j = 0; j < table.recordList.size(); j++) {
					// add records that are not in database
					if (!Excel2Database.database.exist(table, j))
						stmtList.add(table.createInsertStmt(j, false));
					else {
						// add mark
						String setMark = "update " + tableName
								+ " set mark_sh = true "
								+ table.updateWhereClause(j);
						stmtList.add(setMark);
					}
				}// for
				// delete the records whihc are not in the new records
				String deleteRecord = "delete from " + tableName
						+ " where mark_sh is null " + "and identifier='"
						+ identifier + "';";
				stmtList.add(deleteRecord);
				// drop the mark_sh column
				String deleteMark = "alter table " + tableName
						+ " drop column mark_sh;";
				stmtList.add(deleteMark);
			}
			// just insert or update
			else {
				for (int j = 0; j < table.recordList.size(); j++) {
					if (Excel2Database.database.exist(table, j))
						stmtList.add(table.createUpdateStmt(j));
					else
						stmtList.add(table.createInsertStmt(j, false));
				}
			}
		}
		return stmtList;
	}

	public ArrayList<String> createUpdateStmt_version2() throws SQLException,
			IOException {
		ArrayList<String> stmtList = new ArrayList<String>();
		String inseerDatabase = " insert into dataset values( '0', '0','0','1989-12-30',null,"
				+ " '0',null,null,'0',0,'zhanggj@genomics.cn') ;";
		stmtList.add(inseerDatabase);
		String identifier = getTable("dataset").getValue("identifier", 0);
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("dataset")) {
				stmtList.addAll(table.createUpdateStmt());
			}
			// delete or insert
			else if (tableName.equals("file") || tableName.equals("author")
					|| tableName.contains("dataset")) {
				String addColumn = "update " + tableName
						+ " set identifier = '0' " + "where identifier= '"
						+ identifier + "' ;";
				stmtList.add(addColumn);
				// Excel2Database.database.execute(addColumn);
				for (int j = 0; j < table.recordList.size(); j++) {
					// add records that are not in database
					// table.updateWhereClause(index);
					if (!Excel2Database.database.niexist(table, j))
						stmtList.add(table.createInsertStmt(j, false));
					else {
						// update the record, because some attributes other than
						// primary key
						// may be changed
						stmtList.add(table.nicreateUpdateStmt(j));
					}
				}// for
				// delete the records whihc are not in thdee new records
				String deleteRecord = "delete from " + tableName
						+ " where identifier= '0' ;";
				stmtList.add(deleteRecord);
			}
			// just insert or update
			else {
				for (int j = 0; j < table.recordList.size(); j++) {
					if (Excel2Database.database.exist(table, j))
						stmtList.add(table.createUpdateStmt(j));
					else
						stmtList.add(table.createInsertStmt(j, false));
				}
			}
		}
		String deleteDataset = "delete from dataset where identifier= '0';";
		stmtList.add(deleteDataset);
		return stmtList;
	}

	public ArrayList<String> createUpdateStmt_version3() throws SQLException,
			IOException {
		ArrayList<String> stmtList = new ArrayList<String>();
		String inseerDatabase = " insert into dataset values( '0', '0','0','1989-12-30',null,"
				+ " '0',null,null,'0',0,'guoxs@genomics.cn') ;";
		stmtList.add(inseerDatabase);
		String identifier = getTable("dataset").getValue("identifier", 0);
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("dataset")) {
				stmtList.addAll(table.createUpdateStmt());
			}
			// delete or insert
			else if (tableName.equals("file") || tableName.equals("author")
					|| tableName.contains("dataset")) {
				String addColumn = "update " + tableName
						+ " set identifier = '0' " + "where identifier= '"
						+ identifier + "' ;";
				stmtList.add(addColumn);
				// Excel2Database.database.execute(addColumn);
				for (int j = 0; j < table.recordList.size(); j++) {
					// add records that are not in database
					// table.updateWhereClause(index);
					if (Excel2Database.database.completeExist(table, j))
						;
					else if (Excel2Database.database.niexist(table, j)) {
						// update the record, because some attributes other than
						// primary key
						// may be changed
						stmtList.add(table.nicreateUpdateStmt(j));
					}
				}// for
				// delete the records whihc are not in thdee new records
				String deleteRecord = "delete from " + tableName
						+ " where identifier= '0' ;";
				stmtList.add(deleteRecord);
			}
			// just insert or update
			else {
				for (int j = 0; j < table.recordList.size(); j++) {
					if (Excel2Database.database.exist(table, j))
						stmtList.add(table.createUpdateStmt(j));
					else
						stmtList.add(table.createInsertStmt(j, false));
				}
			}
		}
		String deleteDataset = "delete from dataset where identifier= '0';";
		stmtList.add(deleteDataset);
		return stmtList;
	}

	public ArrayList<String> createUpdateStmt_version4() throws SQLException,
			IOException {
		ArrayList<String> stmtList = new ArrayList<String>();

		String identifier = getTable("dataset").getValue("identifier", 0);
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("dataset")) {
				stmtList.addAll(table.createUpdateStmt());
			}
			// delete or insert
			else if (tableName.equals("file") || tableName.equals("author")
					|| tableName.contains("dataset")) {
				// delete old records that are not neeeded
				String delete = "delete from " + tableName
						+ " where identifier='" + identifier +"'";
				if(table.recordList.size()>0)
					delete+=" and not( ";
				else
					delete+= " ;";
				for (int l = 0; l < table.recordList.size(); l++) {
					
					delete+="( ";
					boolean first=true;
					for (int m = 0; m < table.primaryAttributeList.size(); m++) {
						if(table.primaryAttributeList.get(m).equals("identifier"))
							continue;
						if(first)
							first=false;
						else
							delete+=" and ";
						delete += ( table.primaryAttributeList.get(m)
								+ " = " + table.primaryValueList.get(l).get(m));
					}//for
					delete += ")";
					if (l == table.recordList.size() - 1)
						delete += ");";
					else {
						delete += " or ";
					} 
				}//for
//					System.out.println(delete);
					stmtList.add(delete);
					// Excel2Database.database.execute(addColumn);
					for (int j = 0; j < table.recordList.size(); j++) {
						// the records are all the same
						if (Excel2Database.database.completeExist(table, j))
							;
						// only primary attributes are the same
						else if (Excel2Database.database.exist(table,j)) {
							// update the record, because some attributes other
							// than
							// primary key
							// may be changed
							if(!tableName.contains("dataset"))
								stmtList.add(table.createUpdateStmt(j));
						}
						else{
							stmtList.add(table.createInsertStmt(j, false));
						}
					}// for
				}
			// just insert or update
			else {
				for (int j = 0; j < table.recordList.size(); j++) {
					if (Excel2Database.database.exist(table, j))
						stmtList.add(table.createUpdateStmt(j));
					else
						stmtList.add(table.createInsertStmt(j, false));
				}//for
			}//else
		}
		String deleteDataset = "delete from dataset where identifier= '0';";
		stmtList.add(deleteDataset);
		return stmtList;
	}

	public ArrayList<String> naive_createUpdateStmt() throws SQLException,
			IOException {
		ArrayList<String> stmtList = new ArrayList<String>();
		String identifier = getTable("dataset").getValue("identifier", 0);
		for (int i = 0; i < insertOrderList.size(); i++) {
			Table table = tableList.get(insertOrderList.get(i));
			String tableName = table.name;
			if (tableName.equals("dataset")) {
				stmtList.addAll(table.createUpdateStmt());
			}
			// delete or insert
			else if (tableName.equals("file") || tableName.equals("author")
					|| tableName.contains("dataset")) {
				String deleteStmt = "delete from " + tableName
						+ " where identifier='" + identifier + "'";
				stmtList.add(deleteStmt);
				//
				stmtList.addAll(table.createInsertStmt(false));
			}
			// just insert or update
			else {
				for (int j = 0; j < table.recordList.size(); j++) {
					if (Excel2Database.database.exist(table, j))
						stmtList.add(table.createUpdateStmt(j));
					else
						stmtList.add(table.createInsertStmt(j, false));
				}
			}
		}
		return stmtList;
	}

	public void print() {
		System.out.println("the total number of tables: " + tableList.size());
		for (Table table : tableList) {
			table.print();
		}
	}

	public void produceFormatRecordList() {
		for (Table table : tableList) {
			table.produceFormatRecordList();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Schema schema = new Schema();
		// schema.print();
		System.out.println(schema.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath());
	}
}
