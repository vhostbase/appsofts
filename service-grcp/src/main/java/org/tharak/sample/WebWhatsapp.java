package org.tharak.sample;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebWhatsapp {

	/**
	 * Webdriver is chrome, this can be changed to firefox as well.
	 */
	private WebDriver browser = null;
	/**
	 * indicator of if a chat has been started or not
	 */
	private boolean isStarted = false;

	/**
	 * time in millis to wait for checking new incoming message;
	 */
	private long sleepTime = 3000;

	WebWhatsapp() {
		ChromeOptions options = new ChromeOptions();
		//options.setPageLoadStrategy(PageLoadStrategy.NONE);
		browser = new ChromeDriver(options);
		//WebDriverWait wait = new WebDriverWait(browser, 2*60);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("app")));

		/**
		 * path to chrome driver, it has to be installed before using this
		 * program Please check HOWTO.md for installation instruction
		 */
		//System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");
		/**
		 * open web.whatsapp.com and you'll have to add Whatsapp web in your
		 * whatsapp mobile application by scanning the QR Code
		 */
		browser.get("https://web.whatsapp.com");
		
	}

	private void openWhatsapp() {
		/**
		 * wait till whatsapp is loaded after scanning the QR code then type
		 * start in console to start sending messages
		 */
		Scanner sc = new Scanner(System.in);
		String command = sc.next();
		if (!command.equalsIgnoreCase("start")) {
			browser.quit();
			System.exit(1);
		}
		sc.close();

		/**
		 * keep checking for unread count every sleepTime milli secs.
		 * If some elementFound, then click it and set isStarted to true
		 * The while will check for this variable, and then will reply on that element.
		 */
		while (true) {
			try {
				if (isStarted) {
					/**
					 *  once the new messages are found
					 *  get the last message and reply to the user according to it:	
					 */
					WebElement selectedWindow = browser.findElement(By
							.xpath("//div[contains(@class, 'message-list')]"));
					List<WebElement> msgList = selectedWindow.findElements(By
							.xpath("//div[contains(@class,'msg')]"));
					WebElement lastMsgDiv = msgList.get(msgList.size() - 1);
					WebElement lastMsgSpan = lastMsgDiv
							.findElement(By
									.xpath("//span[contains(@class, 'selectable-text')]"));
					String msg = lastMsgSpan.getText();
					reply("I was already chatting with you: " + msg);
					isStarted = false;
				}
				findContact();
				/**
				 *  get the user who just pinged, whose 'unread-count' will be 1 or more
				 */
				List<WebElement> nonSelectedWindows = browser.findElements(By
						.xpath("//div[contains(@class,'X7YrQ')]//span[contains(@title, '+91 95737 25223')]"));
				if (!Utils.isEmptyOrNull(nonSelectedWindows)) {
					isStarted = true;
					responseNonSelectedWindow(nonSelectedWindows);
				} else {
					Utils.log("no new msg yet");
					Thread.sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}

		browser.quit();
	}
	private void searchContact() {
		WebElement panel = browser.findElement(By.xpath("//input[contains(@title,'Search or start new chat')]"));
		panel.sendKeys("Shankarao");
		WebElement contact = browser.findElement(By.xpath("//div[contains(@class,'X7YrQ')]"));
		contact.click();
	}
	private boolean validate(WebElement msgElem) {
		Document doc = Jsoup.parse(msgElem.getAttribute("outerHTML"));
		XPath xPath = XPathFactory.newInstance().newXPath();
		List<Node> nodes = doc.childNodes();
		Iterator<Node> itr = nodes.iterator();
		while(itr.hasNext()) {
			Node node = itr.next();
			try {
			String result = (String)xPath.evaluate("//span[contains(@class, '_F7Vk selectable-text invisible-space copyable-text')]", node, XPathConstants.STRING);
	        System.out.println(result);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			System.out.println(node.outerHtml());
		}
		return false;
	}
	private String getMessage(WebElement msgElem) {
		Document doc = Jsoup.parse(msgElem.getAttribute("outerHTML"));
		List<Node> nodes = doc.childNodes();
		Iterator<Node> itr = nodes.iterator();
		while(itr.hasNext()) {
			Node node = itr.next();
			System.out.println(node.outerHtml());
			try {
				String result = ((Element)node).select("span[class *= copyable-text]>span").text();
				return result;
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		return null;
	}
	static int count = 0;
	private void findContact() {
			try {
				WebElement txtElem = null;
				WebElement panel = browser.findElement(By.xpath("//div[contains(@class,'X7YrQ')]"));
				try {
					txtElem = panel.findElement(By.xpath("//div[contains(@class,'_3u328 copyable-text selectable-text')]"));
				}catch (Exception e) {
				}
				if(txtElem == null) {
					WebElement elem = panel.findElement(By.xpath("//span[contains(@class,'P6z4j')]"));
					//validate(elem);
					panel.click();
				}
				List<WebElement> msgElems = browser.findElements(By.xpath("//div[contains(@class,'message-in')]"));
				if(count == 0) {
					if(msgElems.size() > count) {
						doSend(msgElems, panel);
					}
				}else {
				}
			}catch(Throwable th) {
				
			}
	}
	private void doSend(List<WebElement> msgElems, WebElement panel) {
		count = msgElems.size();
		String latestMsg = getMessage(msgElems.get(count - 1));
		System.out.println(latestMsg);
		WebElement txtElem = panel.findElement(By.xpath("//div[contains(@class,'_3u328 copyable-text selectable-text')]"));
		txtElem.sendKeys("Hello");
		WebElement btnElem = panel.findElement(By.xpath("//button[contains(@class,'_3M-N-')]"));
		btnElem.click();
	}
	/**
	 * Select the user with unread-count and click on it to start chatting with him/her
	 * @param elems
	 */
	private void responseNonSelectedWindow(List<WebElement> elems) {
		Utils.log("new msgs found");
		for (WebElement elem : elems) {
			elem.click();
			reply("your chat was not selected. Now it is.");
		}
	}

	private void reply(String string) {
		List<WebElement> elem1 = browser.findElements(By.className("input"));
		for (int i = 0; i < 1; i++) {
			elem1.get(1).sendKeys(string);//
			browser.findElement(By.className("send-container")).click();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.silentOutput", "true");
		System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");
		WebWhatsapp web = new WebWhatsapp();
		web.openWhatsapp();
	}

}
