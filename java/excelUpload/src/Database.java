
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import Log.Log;

public class Database {
	String url;
	String user;
	String password;
	Connection con;
	Statement stmt;
	public Database() throws Exception{
		Setting.Loadsetting();
		url=Setting.databaseUrl;
		password=Setting.databasePassword;
		user=Setting.databaseUserName;
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			con = DriverManager.getConnection(url, user, password);
			//this is important
			con.setAutoCommit(false);
			stmt = con.createStatement();
			ResultSet resultSet=stmt.executeQuery("select * from submitter");
//			int i=1;
//			while(resultSet.next())
//				System.out.println(resultSet.getString(4));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Database(String url, String user, String password) throws SQLException {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.user = user;
		this.password = password;
		// 锟斤拷锟斤拷锟斤拷
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, user, password); // 使锟斤拷SQL-SERVER2000锟斤拷证
			stmt = con.createStatement();
			stmt.execute("selcet * from submitter");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean exist(String tableName,String field,String value) throws SQLException{
		String query="select null from "+tableName+" where "+
			field+" = '"+value+"';";
		ResultSet resultSet=stmt.executeQuery(query);
		if(resultSet.next()){
			return true;
		}
		return false;
	}
	public ResultSet getResultSet(String query) throws SQLException{
		return stmt.executeQuery(query);
	}
	// to dertermine if a record exists.
	public boolean exist(Table table,int index) throws SQLException{
		String query="select null from "+table.name+table.updateWhereClause(index);
		ResultSet resultSet=stmt.executeQuery(query);
		if(resultSet.next()){
			return true;
		}
		return false;
	}
	// to dertermine if a record exists, all attributes should equal
	public boolean completeExist(Table table,int index) throws SQLException{
		String query="select null from "+table.name+table.completeWhereClause(index);
		System.out.println(query);
		ResultSet resultSet=stmt.executeQuery(query);
		if(resultSet.next()){
			return true;
		}
		return false;
	}
	// to dertermine if a record exists.
	public boolean niexist(Table table,int index) throws SQLException{
		String query="select null from "+table.name+table.niWhereClause(index);
		ResultSet resultSet=stmt.executeQuery(query);
		if(resultSet.next()){
			return true;
		}
		return false;
	}
//	public Table getTable(String tableName){
//		
//	}
//	public Object get(String table,String field,String value) throws SQLException{
//		String query="select null from "+table+" where "+
//			field+" = '"+value+"';";
//		ResultSet resultSet=stmt.executeQuery(query);
//		if(resultSet.next()){
//			return true;
//		}
//		return false;
//	}
	public long execute(ArrayList<String> stmtList) throws SQLException, IOException {
//		stmt.execute("begin;");
//		String rollBack="rollback;";
		long startTime=System.currentTimeMillis();
		for(String s:stmtList){
			try{
				stmt.execute(s);
			}
			catch (Exception e) {
//				stmt.execute(rollBack);
				// TODO: handle exception
				Excel2Database.excel2DBLog.writeLine("**************************");
				Excel2Database.excel2DBLog.writeLine("SQL statement: "+s);
				e.printStackTrace(Excel2Database.excel2DBLog.printWriter);
//				System.out.println(stmts[i]);
				Excel2Database.excel2DBLog.writeLine(e.toString());
				Excel2Database.excel2DBLog.writeLine("**************************");
				throw new SQLException(e);
			}
		}
		long endTime=System.currentTimeMillis();
		long time=endTime-startTime;
		System.out.println("execution time: "+ time);
//		stmt.execute("commit;");
		return time;
	}
	public void execute(String s) throws SQLException, IOException {
//		stmt.execute("begin;");
//		String rollBack="rollback;";
			try{
				stmt.execute(s);
			}
			catch (Exception e) {
//				stmt.execute(rollBack);
				// TODO: handle exception
				Excel2Database.excel2DBLog.writeLine("**************************");
				Excel2Database.excel2DBLog.writeLine(s);
				Excel2Database.excel2DBLog.writeLine(e.toString());
				Excel2Database.excel2DBLog.writeLine("**************************");
				throw new SQLException(e);
			}
//		stmt.execute("commit;");
	}
	public static void main(String[] args) throws Exception {
		Database database = new Database();			
//		database.calPhraseProb();
//		System.out.println(database.exist("1.5524/100003"));
	}


}
