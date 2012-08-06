/**
 * 
 */
package Test;

import java.io.IOException;
import java.net.MalformedURLException;

import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



/**
 * @author 王森洪
 *
 * @date 2012-4-23
 */
public class htmlUnit {
  static public void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
	  WebClient webClient=new WebClient();
	  //setting
	  webClient.setJavaScriptEnabled(true);
	  webClient.setCssEnabled(true);
	  
	  HtmlPage currentPage = webClient.getPage("http://www.ebi.ac.uk/ena/data/view/123");
	  webClient.waitForBackgroundJavaScript(300000);
	  String Source = currentPage.asXml();
	  System.out.println(Source);
	  currentPage.executeJavaScript(currentPage.asText());

	  Element element=currentPage.getElementById("enaIndexerContents");
//	  Element tableElement=(Element) element.getFirstChild();
	  
	  System.out.println(element.getTextContent());
//	  String Source = currentPage.asXml();
//	  System.out.println(Source);
//	  System.out.println("Title: "+currentPage.getTitleText());
//	  System.out.println("Text: "+currentPage.getTitleText());
//	  element =currentPage.getDocumentElement();
//	  System.out.println(element.toString());
  }
}
