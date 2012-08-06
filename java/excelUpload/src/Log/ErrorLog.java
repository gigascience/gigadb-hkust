/**
 * 
 */
package Log;


/**
 * @author 王森洪
 *
 * @date 2012-3-8
 */
public class ErrorLog extends Log{

	/**
	 * 
	 */
	public ErrorLog() {
		// TODO Auto-generated constructor stub
	}
	//写错误日志到文件里
	public ErrorLog(String fileName,boolean append) throws Exception{
		super(fileName,append);
	}
	
}
