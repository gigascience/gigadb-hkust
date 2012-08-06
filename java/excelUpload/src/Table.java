import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.lang.model.element.Element;

import sun.font.CreatedFontTracker;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-3-31
 */
public class Table {
	String name;
	ArrayList<String> attributeList;
	ArrayList<String> typeList;
	ArrayList<String> primaryAttributeList;

	ArrayList<ArrayList<String>> recordList;
	ArrayList<ArrayList<String>> primaryValueList;
	ArrayList<ArrayList<String>> formatRecordList;
	int insertOrder;

	public Table() {
		insertOrder = 0;
		name = new String();
		attributeList = new ArrayList<String>();
		typeList = new ArrayList<String>();
		recordList = new ArrayList<ArrayList<String>>();
		primaryAttributeList = new ArrayList<String>();
		primaryValueList = new ArrayList<ArrayList<String>>();
	}

	public int getIndex(String attribute) {
		for (int i = 0; i < attributeList.size(); i++) {
			if (attributeList.get(i).equals(attribute))
				return i;
		}
		try {
			Excel2Database.excel2DBLog.writeLine("Can't find the attribute: "
					+ attribute + " in table " + name);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	// create stmt for record i
	public String createInsertStmt(int index,boolean ignoreExist) {
		ArrayList<String> record = getFormatRecord(index);
		String stmt=null;
		if(ignoreExist){
			stmt = "insert into " + name;
			stmt += " select ";
			for (int i = 0; i < attributeList.size(); i++) {
				stmt += record.get(i);
				if (i == (record.size() - 1))
					stmt += " ";
				else
					stmt += ", ";
			}
			// where clause
			stmt += insertWhereClause(index);
		}
		else{
			 stmt = "insert into " + name;
			stmt += " values( ";
			for (int i = 0; i < attributeList.size(); i++) {
				stmt += record.get(i);
				if (i == (record.size() - 1))
					stmt += " );";
				else
					stmt += ", ";
			}
		}
		return stmt;
	}

	// create stmt for record i
	public String nicreaateInsertStmt(int index) {
		ArrayList<String> record = getFormatRecord(index);
		String stmt = "insert into " + name;
		stmt += " select ";
		for (int i = 0; i < attributeList.size(); i++) {
			stmt += record.get(i);
			if (i == (record.size() - 1))
				stmt += " ";
			else
				stmt += ", ";
		}
		// where clause
		stmt += niinsertWhereClause(index);

		return stmt;
	}

	public ArrayList<String> createInsertStmt(boolean ignoreExist) {
		ArrayList<String> stmtList = new ArrayList<String>();
		//
		// there is not records
		if (recordList.size() == 0)
			return stmtList;

		for (int j = 0; j < recordList.size(); j++) {
			String stmt = createInsertStmt(j, ignoreExist);
			stmtList.add(stmt);
		}

		return stmtList;
	}

	public String createUpdateStmt(int index) {
		String stmt = "update " + name;
		stmt += " set ";
		ArrayList<String> record = getFormatRecord(index);
		for (int i = 0; i < attributeList.size(); i++) {
			stmt += (attributeList.get(i) + " = ");
			stmt += record.get(i);
			if (i == (record.size() - 1))
				stmt += " ";
			else
				stmt += ", ";
		}
		// where clause
		stmt += updateWhereClause(index);
		return stmt;
	}

	public String nicreateUpdateStmt(int index) {
		String stmt = "update " + name;
		stmt += " set ";
		ArrayList<String> record = getFormatRecord(index);
		for (int i = 0; i < attributeList.size(); i++) {
			stmt += (attributeList.get(i) + " = ");
			stmt += record.get(i);
			if (i == (record.size() - 1))
				stmt += " ";
			else
				stmt += ", ";
		}
		// where clause
		stmt += niWhereClause(index);
		return stmt;
	}

	public ArrayList<String> createUpdateStmt() {
		ArrayList<String> stmtList = new ArrayList<String>();
		//
		// there is not records
		if (recordList.size() == 0)
			return stmtList;
		String stmts = new String();
		for (int j = 0; j < recordList.size(); j++) {
			String stmt = createUpdateStmt(j);
			stmtList.add(stmt);
		}
		return stmtList;
	}

	// fill primary value for each record
	void fillPrimaryValueList() {
		for (int k = 0; k < recordList.size(); k++) {
			ArrayList<String> record = getFormatRecord(k);
			ArrayList<String> primaryValue = new ArrayList<String>();
			for (int j = 0; j < primaryAttributeList.size(); j++) {
				int i = getIndex(primaryAttributeList.get(j));
				primaryValue.add(record.get(i));
			}// for
			primaryValueList.add(primaryValue);
		}
	}

	// primary keys
	String insertWhereClause(int index) {
		String where = "where not exists ( select null from " + name
				+ " where ";
		// it has been famated
		ArrayList<String> primaryValue = primaryValueList.get(index);
		for (int j = 0; j < primaryAttributeList.size(); j++) {
			if (primaryValue.get(j) == null)
				where += (primaryAttributeList.get(j) + " is null");
			else
				where += (primaryAttributeList.get(j) + " = " + primaryValue
						.get(j));
			if (j == primaryAttributeList.size() - 1)
				where += " ); ";
			else
				where += " and ";
		}
		return where;
	}

	// primary keys
	String niinsertWhereClause(int index) {
		String where = "where not exists ( select null from " + name
				+ " where ";
		// it has been famated
		ArrayList<String> primaryValue = primaryValueList.get(index);
		for (int j = 0; j < primaryAttributeList.size(); j++) {
			if (primaryAttributeList.get(j).equals("identifier"))
				where += (primaryAttributeList.get(j) + " = '0' ");
			else if (primaryValue.get(j) == null)
				where += (primaryAttributeList.get(j) + " is null");
			else
				where += (primaryAttributeList.get(j) + " = " + primaryValue
						.get(j));
			if (j == primaryAttributeList.size() - 1)
				where += " ); ";
			else
				where += " and ";
		}
		return where;
	}

	String fullWhereClause(int index) {
		String where = " where ";
		ArrayList<String> valueList = recordList.get(index);
		for (int j = 0; j < attributeList.size(); j++) {
			if (valueList.get(j) == null)
				where += (attributeList.get(j) + " is null");
			else
				where += (attributeList.get(j) + " = " + valueList.get(j));
			if (j == attributeList.size() - 1)
				where += " ; ";
			else
				where += " and ";
		}
		return where;
	}

	String updateWhereClause(int index) {
		String where = " where ";
		ArrayList<String> primaryValue = primaryValueList.get(index);
		for (int j = 0; j < primaryAttributeList.size(); j++) {
			if (primaryValue.get(j) == null)
				where += (primaryAttributeList.get(j) + " is null");
			else
				where += (primaryAttributeList.get(j) + " = " + primaryValue
						.get(j));
			if (j == primaryAttributeList.size() - 1)
				where += " ; ";
			else
				where += " and ";
		}
		return where;
	}
	String completeWhereClause(int index) {
		String where = " where ";
		ArrayList<String> valueList= formatRecordList.get(index);
		for (int j = 0; j <valueList.size(); j++) {
//			if (attributeList.get(j).equals("identifier"))
//				where += (attributeList.get(j) + " = '0' ");
			if (valueList.get(j) == null)
				where += (attributeList.get(j) + " is null");
			else
				where += (attributeList.get(j) + " = " + valueList
						.get(j));
			if (j == valueList.size() - 1)
				where += " ; ";
			else
				where += " and ";
		}
		return where;
	}
	String niWhereClause(int index) {
		String where = " where ";
		ArrayList<String> primaryValue = primaryValueList.get(index);
		for (int j = 0; j < primaryAttributeList.size(); j++) {
			if (primaryAttributeList.get(j).equals("identifier"))
				where += (primaryAttributeList.get(j) + " = '0' ");
			else if (primaryValue.get(j) == null)
				where += (primaryAttributeList.get(j) + " is null");
			else
				where += (primaryAttributeList.get(j) + " = " + primaryValue
						.get(j));
			if (j == primaryAttributeList.size() - 1)
				where += " ; ";
			else
				where += " and ";
		}
		return where;
	}

	ArrayList<String> getFormatRecord(int index) {
		return formatRecordList.get(index);
	}

	// add quotes
	void produceFormatRecordList() {
		formatRecordList = HelpFunctions.copyList(recordList);
		for (int i = 0; i < formatRecordList.size(); i++) {
			ArrayList<String> record = formatRecordList.get(i);
			for (int j = 0; j < record.size(); j++) {
				String value = record.get(j);
				if (value == null)
					record.set(j, "null");
				else if (value.equals("")) {
					if (typeList.get(j).contains("date"))
						record.set(j, "'1976-1-1'");
					else if (typeList.get(j).equalsIgnoreCase("integer")
							|| typeList.get(j).equalsIgnoreCase("bigint"))
						record.set(j, "0");
					else
						record.set(j, "''");
				} else {
					// char type
					// decide if double quatation is needed
					// if(j==4 && typeList.size()==4){
					// System.out.println(name);
					// }
					if (typeList.get(j).equalsIgnoreCase("integer")
							|| typeList.get(j).equalsIgnoreCase("bigint"))
						;
					else if (typeList.get(j).equalsIgnoreCase("date"))
						record.set(j, "'" + (record.get(j)) + "'");
					else {
						record.set(j, "'" + process(record.get(j)) + "'");
					}
				}
			}
		}
	}

	// just valid if the table just have only one record.
	// return the value of attribute
	public String getValue(String attribute, int index) {
		for (int i = 0; i < attributeList.size(); i++) {
			if (attributeList.get(i).equals(attribute)) {
				// make sure recordList is not null
				if (recordList.size() == 0)
					return null;
				return recordList.get(index).get(i);
			}
		}
		return null;
	}

	public void setValue(String attribute, String value, int index) {
		int i = getIndex(attribute);
		recordList.get(index).set(i, value);
	}

	// // just valid
	// public String setValue(String attribute,String value){
	// int index=getIndex(attribute);
	// re
	// }
	public String process(String temp) {
		return temp.replaceAll("'", "''");
	}

	public void print() {
		System.out.println("Table: " + name);
		System.out.println("Attribute: " + attributeList.toString());
		for (int i = 0; i < recordList.size(); i++) {
			ArrayList<String> record = recordList.get(i);
			System.out.println("record " + i + ": ");
			for (int j = 0; j < attributeList.size(); j++) {
				System.out
						.println(attributeList.get(j) + " : " + record.get(j));
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
