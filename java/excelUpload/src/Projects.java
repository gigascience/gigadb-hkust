import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-4-4
 */
public class Projects {
	HashMap<String, String> image_locatonMap;
	/**
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 */
	public Projects() throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated constructor stub
		image_locatonMap=HelpFunctions.getMap2("configuration/project-image_location.xml");
	}
	HashMap<String, String> getDetails(String url) {
		String[] temp = url.split(":", 2);

		String project_name = temp[0].trim();

		int name_length = project_name.length();
		// delete double quotes at begin and end of the string
		project_name = project_name.substring(1, name_length - 1);
		// project_url
		url = temp[1].trim();
		
		
		HashMap<String,String>details = new HashMap<String, String>();
		details.put("project_name", project_name);
		//get image location
		String image_location= image_locatonMap.get(project_name);
		details.put("project_image_location",image_location);
		details.put("project_url", url);
		return details;
	}

	static String getName(String url) {
		int beginIndex = url.indexOf("www.") + 4;
		int endIndex = url.indexOf(".", beginIndex);
		return url.substring(beginIndex, endIndex);
	}

}
