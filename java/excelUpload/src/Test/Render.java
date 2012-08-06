package Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mozilla.jrex.JRexFactory;
import org.mozilla.jrex.event.progress.ProgressEvent;
import org.mozilla.jrex.navigation.WebNavigation;
import org.mozilla.jrex.navigation.WebNavigationConstants;
import org.mozilla.jrex.ui.JRexCanvas;
import org.mozilla.jrex.window.JRexWindowManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
  
public class Render implements org.mozilla.jrex.event.progress.ProgressListener {  
  
boolean done = false;  
  
public boolean parsePage(String url) throws Exception {  
	System.setProperty("jrex.dom.enabled","true");
	

   System.setProperty("jrex.browser.usesetupflags", "true");  
  
   System.setProperty("jrex.browser.allow.images", "false"); //不加载图片  
  
   System.setProperty("jrex.browser.allow.plugin", "false"); //不加载flash  
  
// The JRexCanvas is the main browser component. The WebNavigator  
  
   // is used to access the DOM.  
  
   JRexCanvas canvas = null;  
  
   WebNavigation navigation = null;  
  
   // Start up JRex/Gecko.  
  
   JRexFactory.getInstance().startEngine();  
  
   // Get a window manager and put the browser in a Swing frame.  
  
   // Based on Dietrich Kappe's code.  
  
   JRexWindowManager winManager = (JRexWindowManager) JRexFactory  
  
   .getInstance().getImplInstance(JRexFactory.WINDOW_MANAGER);  
  
   winManager.create(JRexWindowManager.SINGLE_WINDOW_MODE);  
  
   JPanel panel = new JPanel();  
  
   JFrame frame = new JFrame();  
  
   frame.getContentPane().add(panel);  
  
   winManager.init(panel);  
  
   // Get the JRexCanvas, set Render to handle progress events so  
  
   // we can determine when the page is loaded, and get the  
  
   // WebNavigator object.  
  
   canvas = (JRexCanvas) winManager.getBrowserForRootParent(panel).next();  
  
   canvas.addProgressListener(this);  
  
   navigation = canvas.getNavigator();  
  
   // Load and process the page.  
  
   navigation.loadURI(url, WebNavigationConstants.LOAD_FLAGS_NONE, null,  
  
   null, null);  
  
   // Swing magic.  
  
   frame.setSize(640, 480);  
  
   frame.setVisible(false);  
  
   // Check if the DOM has loaded every two seconds.  
//  
//   while (!done) {  
//   navigation.ge
   Document doc = navigation.getDocument();  
   if(doc==null){
	   System.out.println("before thread: doc is null");
   }
   else{
	   System.out.println("before thread: doc is not null");
   }
    Thread.sleep(10*1000);  
  
//   }  
  
   // Get the DOM and recurse on its nodes.  
  
  doc = navigation.getDocument();  
  
   Element ex = doc.getDocumentElement();  
  
    
File file = new File("d:\\youtube.html");  
FileOutputStream outer = new FileOutputStream(file);  
OutputStreamWriter sw = new OutputStreamWriter(outer,"utf-8");  
sw.write(xmlToString(ex));  
sw.close();  
  
System.out.println(xmlToString(ex));  
  
   return true;  
  
}  
  
public static String xmlToString(Node node) throws Exception {  
  
   Source source = new DOMSource(node);  
  
   StringWriter stringWriter = new StringWriter();  
  
   Result result = new StreamResult(stringWriter);  
  
   TransformerFactory factory = TransformerFactory.newInstance();  
  
   Transformer transformer = factory.newTransformer();  
  
   transformer.setOutputProperty(OutputKeys.METHOD, "html");  
  
   transformer.transform(source, result);  
  
   return stringWriter.getBuffer().toString();  
  
}  
  
/** 
 
* onStateChange is invoked several times when DOM loading is complete. Set 
 
* the done flag the first time. 
 
*/  
  
public void onStateChange(ProgressEvent event) {  
  
   if (!event.isLoadingDocument()) {  
	   System.out.println("I am not liading now");
	   if (done)  
		   return;
   }
   else{
	   done = true;  
	   System.out.println("I am  liading now");
   }
   
  
}  
  
public static void main(String[] args) throws Exception {  
  
    
//String url = "http://www.youtube.com/watch?v=XOHE2KsmdGg";  
//String url = "http://www.cnn.com";  
//String url = "http://www.56.com/u42/v_MzY2NTYxNjc.html";  
String url = "http://ilovelate.blog.163.com";  
  
Render p = new Render();  
  
   p.parsePage(url);  
  
   System.exit(0);  
  
}  
  
public void onLinkStatusChange(ProgressEvent event) {  
  
}  
  
public void onLocationChange(ProgressEvent event) {  
  
}  
  
public void onProgressChange(ProgressEvent event) {  
  
}  
  
public void onSecurityChange(ProgressEvent event) {  
  
}  
  
public void onStatusChange(ProgressEvent event) {  
  
}  
  
}  