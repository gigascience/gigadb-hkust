/**
 * 
 */
package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Log.Log;

/**
 * @author 王森洪
 * 
 * @date 2012-4-26
 */
public class ReadFile {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("a.txt"));
		String line = null;
		// String[] parameter = null;
		Log log = new Log("test.txt", false);
		while ((line = reader.readLine()) != null) {
			log.writeLine(line);
		}
	}
}
