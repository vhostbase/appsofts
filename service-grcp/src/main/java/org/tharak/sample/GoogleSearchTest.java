package org.tharak.sample;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GoogleSearchTest {
	
	public static void main(String...args) {
		
		WebDriver driver  = new ChromeDriver();
		driver.navigate().to("http://google.com");
		String appTitle = driver.getTitle();
		System.out.println("Application title is :: "+appTitle);
		driver.quit();
	}

}
