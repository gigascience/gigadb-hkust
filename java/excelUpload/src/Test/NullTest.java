/**
 * 
 */
package Test;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author 王森洪
 *
 * @date 2012-4-25
 */
public class NullTest {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, SQLException {
		String aString=null;
		aString="asdfa";
		System.out.println(aString);
	}
}
