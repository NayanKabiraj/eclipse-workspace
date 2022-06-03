package com.crm.qa.testScenarios;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.crm.qa.base.BasePage;
import com.crm.qa.base.Page;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	
	WebDriver driver;
	public Page page;
	
	@BeforeMethod
	@Parameters(value= {"browser"})
	public void setup(String browser) {
		if(browser.equals("chrome")) {
			WebDriverManager.chromedriver();
			driver=new ChromeDriver();
		}
		else if (browser.equals("Firefox")) {
			WebDriverManager.firefoxdriver();
			driver=new FirefoxDriver();
		}
		else {
			System.out.println("No browser input found");
		}
		driver.get("http://www.google.com");
		page = new BasePage(driver);
			
	}
	
	@AfterMethod
	public void teardown() {
		driver.quit();
	}

}
