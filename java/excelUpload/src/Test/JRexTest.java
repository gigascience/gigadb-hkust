package Test;

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
import org.mozilla.jrex.exception.JRexException;
import org.mozilla.jrex.navigation.WebNavigation;
import org.mozilla.jrex.navigation.WebNavigationConstants;
import org.mozilla.jrex.ui.JRexCanvas;
import org.mozilla.jrex.window.JRexWindowManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.DocumentRange;

public class JRexTest {
	
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
	public static void main(String[] args) throws DOMException, Exception {
		try {
			JRexFactory.getInstance().startEngine();
		} catch (Exception e) {
			System.err.println("Unable to start up JRex Engine.");
			e.printStackTrace();
			System.exit(1);
		}
		JRexWindowManager winManager = (JRexWindowManager) JRexFactory
				.getInstance().getImplInstance(JRexFactory.WINDOW_MANAGER);
		winManager.create(JRexWindowManager.SINGLE_WINDOW_MODE);
		JPanel inner = new JPanel();
		JFrame frame = new JFrame();
		frame.getContentPane().add(inner);
		winManager.init(inner);
		frame.setSize(640, 480);
		frame.setVisible(true);
		JPanel panel = new JPanel();  
		JRexCanvas canvas = null;
		String url="http://ilovelate.blog.163.com";
		WebNavigation navigation = null;
		 canvas = (JRexCanvas) winManager.getBrowserForParent(panel);  
		navigation = canvas.getNavigator();
		navigation.loadURI(url, WebNavigationConstants.LOAD_FLAGS_NONE, null,
				null, null);
		Document doc = navigation.getDocument();
		Element ex = doc.getDocumentElement();
		DocumentRange range=((org.mozilla.jrex.dom.JRexDocumentImpl)doc).getDocumentRange();
		System.out.println(xmlToString(range.createRange().getCommonAncestorContainer()));
//		System.out.println(ex.getTextContent()); //prints page source
	}
}