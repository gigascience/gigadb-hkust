/**
 * 
 */
package Test;

//import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author 王森洪
 *
 * @date 2012-4-19
 */
public class Date{

	public static void  main(String[] args){
		TimeZone defaultTimeZone=TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar calendar=Calendar.getInstance();
		DateFormat dateFormat=new SimpleDateFormat("@yyyyMMddHHmm");
		String postfix=dateFormat.format(calendar.getTime());
		TimeZone.setDefault(defaultTimeZone);
		System.out.println(postfix);
	}
}
