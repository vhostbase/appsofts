package org.tharak.sample;

import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class TestPath {

	public static void main(String[] args) {
		String input = "<html>\r\n" + 
				" <head></head>\r\n" + 
				" <body>\r\n" + 
				"  <div class=\"FTBzM message-in\">\r\n" + 
				"   <span></span>\r\n" + 
				"   <div class=\"_1zGQT _2ugFP\">\r\n" + 
				"    <div class=\"_2Wx_5 _3LG3B\">\r\n" + 
				"     <div class=\"-N6Gq\">\r\n" + 
				"      <div class=\"copyable-text\" data-pre-plain-text=\"[5:24 PM, 2/29/2020] Shankarao Prosoft: \">\r\n" + 
				"       <div class=\"_12pGw\" dir=\"ltr\">\r\n" + 
				"        <span dir=\"ltr\" class=\"_F7Vk selectable-text invisible-space copyable-text\"><span>Hi</span></span>\r\n" + 
				"        <span class=\"EopGb\"></span>\r\n" + 
				"       </div>\r\n" + 
				"      </div>\r\n" + 
				"      <div class=\"_1RNhZ\">\r\n" + 
				"       <div class=\"_3MYI2\">\r\n" + 
				"        <span class=\"_3fnHB\" dir=\"auto\">5:24 PM</span>\r\n" + 
				"       </div>\r\n" + 
				"      </div>\r\n" + 
				"     </div>\r\n" + 
				"    </div>\r\n" + 
				"    <span></span>\r\n" + 
				"   </div>\r\n" + 
				"  </div>\r\n" + 
				" </body>\r\n" + 
				"</html>";
		
		Document doc = Jsoup.parse(input);
		XPath xPath = XPathFactory.newInstance().newXPath();
		List<Node> nodes = doc.childNodes();
		Iterator<Node> itr = nodes.iterator();
		while(itr.hasNext()) {
			Node node = itr.next();
			System.out.println(node.outerHtml());
			try {
				String result = ((Element)node).select("span[class *= copyable-text]>span").text();
			//String result = (String)xPath.evaluate("//span[contains(@class, '_F7Vk selectable-text invisible-space copyable-text')]", node, XPathConstants.STRING);
	        System.out.println(result);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}

}
